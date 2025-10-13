package com.cezila.feature_avalanche

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Round
import com.cezila.domain.use_case.StartAvalancheRoundUseCase
import com.cezila.domain.use_case.SubmitGuessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.cezila.domain.Result
import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.error.SubmitGuessError
import com.cezila.domain.model.LetterFeedback
import com.cezila.domain.model.RoundStatus

@HiltViewModel
class AvalancheViewModel @Inject constructor(
    private val startRound: StartAvalancheRoundUseCase,
    private val submitGuess: SubmitGuessUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AvalancheUiState())
    val state: StateFlow<AvalancheUiState> = _state

    fun start() {
        _state.value = _state.value.copy(
            loading = true,
            submitError = null,
            startRoundError = null,
            rows = emptyList(),
            currentInput = "",
            keyStates = emptyMap()
        )
        viewModelScope.launch {
            when (val res = startRound()) {
                is Result.Error -> _state.value =
                    _state.value.copy(loading = false, startRoundError = res.error)

                is Result.Success -> _state.value =
                    _state.value.copy(loading = false, round = res.data)
            }
        }
    }

    fun onKey(ch: Char) {
        val s = _state.value
        if (s.finished || s.round == null) return
        if (s.currentInput.length >= 5) return
        _state.value = s.copy(currentInput = s.currentInput + ch)
    }

    fun onBackspace() {
        val s = _state.value
        if (s.currentInput.isEmpty()) return
        _state.value = s.copy(currentInput = s.currentInput.dropLast(1))
    }

    fun onEnter() {
        val s = _state.value
        val round = s.round ?: return
        if (s.currentInput.length != 5) return
        viewModelScope.launch {
            when (val res = submitGuess(round, s.currentInput)) {
                is Result.Error -> _state.value = s.copy(submitError = res.error)
                is Result.Success -> {
                    val out = res.data
                    val row = out.result.feedback.map { it.letter to it.state }
                    _state.value = s.copy(
                        round = out.round,
                        rows = s.rows + listOf(row),
                        currentInput = "",
                        finished = out.round.status != RoundStatus.IN_PROGRESS,
                        won = out.round.status == RoundStatus.WON,
                        submitError = null,
                        keyStates = mergeKeyStates(s.keyStates, out.result.feedback)
                    )
                }
            }
        }
    }

    fun toggleHelp(show: Boolean) {
        _state.value = _state.value.copy(showHelp = show)
    }

    private fun mergeKeyStates(
        old: Map<Char, LetterState>,
        feedback: List<LetterFeedback>
    ): Map<Char, LetterState> {
        val pr = mapOf(LetterState.ABSENT to 0, LetterState.PRESENT to 1, LetterState.CORRECT to 2)
        val m = old.toMutableMap()
        feedback.forEach { f ->
            val cur = m[f.letter]
            if (cur == null || pr[f.state]!! > pr[cur]!!) m[f.letter] = f.state
        }
        return m
    }
}

data class AvalancheUiState(
    val loading: Boolean = true,
    val round: Round? = null,
    val rows: List<List<Pair<Char, LetterState>>> = emptyList(),
    val currentInput: String = "",
    val finished: Boolean = false,
    val won: Boolean = false,
    val submitError: SubmitGuessError? = null,
    val startRoundError: StartAvalancheRoundError? = null,
    val showHelp: Boolean = false,
    val keyStates: Map<Char, LetterState> = emptyMap()
)