package com.cezila.feature_avalanche

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.Result
import com.cezila.domain.error.SubmitGuessError
import com.cezila.domain.model.LetterFeedback
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.model.Word
import com.cezila.domain.rules.GuessEvaluator
import com.cezila.domain.use_case.LoadCurrentRoundUseCase
import com.cezila.domain.use_case.SaveRoundUseCase
import com.cezila.domain.use_case.StartAvalancheRoundUseCase
import com.cezila.domain.use_case.SubmitGuessUseCase
import com.cezila.domain.use_case.UpdateGameResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvalancheViewModel @Inject constructor(
    private val startRound: StartAvalancheRoundUseCase,
    private val submitGuess: SubmitGuessUseCase,
    private val loadCurrentRound: LoadCurrentRoundUseCase,
    private val saveRound: SaveRoundUseCase,
    private val updateGameResult: UpdateGameResultUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AvalancheUiState())
    val state: StateFlow<AvalancheUiState> = _state.asStateFlow()

    private val _events = Channel<AvalancheEvent>()
    val events = _events.receiveAsFlow()

    fun start() {
        _state.update {
            it.copy(
                loading = true,
                startRoundFailed = false,
                rows = emptyList(),
                currentInput = "",
                keyStates = emptyMap()
            )
        }
        viewModelScope.launch {
            val savedRound = loadCurrentRound().first()
            if (savedRound != null) {
                val rows = savedRound.attempts.map { attempt ->
                    val result = GuessEvaluator.evaluate(savedRound.target, attempt)
                    result.feedback.map { it.letter to it.state }
                }
                val keyStates = mergeKeyStatesFromAttempts(savedRound.attempts, savedRound.target)
                _state.update {
                    it.copy(
                        loading = false,
                        round = savedRound,
                        rows = rows,
                        keyStates = keyStates
                    )
                }
            } else {
                when (val res = startRound()) {
                    is Result.Error -> _state.update {
                        it.copy(loading = false, startRoundFailed = true)
                    }
                    is Result.Success -> _state.update {
                        it.copy(
                            loading = false,
                            round = res.data,
                            rows = emptyList(),
                            currentInput = "",
                            keyStates = emptyMap()
                        )
                    }
                }
            }
        }
    }

    fun onKey(ch: Char) {
        val s = _state.value
        if (s.finished || s.round == null) return
        if (s.currentInput.length >= 5) return
        _state.update { it.copy(currentInput = it.currentInput + ch) }
    }

    fun onBackspace() {
        val s = _state.value
        if (s.currentInput.isEmpty()) return
        _state.update { it.copy(currentInput = it.currentInput.dropLast(1)) }
    }

    fun onEnter() {
        val s = _state.value
        val round = s.round ?: return
        if (s.currentInput.length != 5) return
        viewModelScope.launch {
            when (val res = submitGuess(round, s.currentInput)) {
                is Result.Error -> {
                    _events.send(AvalancheEvent.ShowError(res.error))
                }
                is Result.Success -> {
                    val out = res.data
                    val updatedRound = out.round
                    val row = out.result.feedback.map { it.letter to it.state }
                    _state.update {
                        it.copy(
                            round = updatedRound,
                            rows = it.rows + listOf(row),
                            currentInput = "",
                            finished = updatedRound.status != RoundStatus.IN_PROGRESS,
                            won = updatedRound.status == RoundStatus.WON,
                            keyStates = mergeKeyStates(it.keyStates, out.result.feedback)
                        )
                    }
                    if (updatedRound.status != RoundStatus.IN_PROGRESS) {
                        updateGameResult(updatedRound.status == RoundStatus.WON)
                    } else {
                        saveRound(updatedRound)
                    }
                }
            }
        }
    }

    fun toggleHelp(show: Boolean) {
        _state.update { it.copy(showHelp = show) }
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

    private fun mergeKeyStatesFromAttempts(
        attempts: List<Word>,
        target: Word
    ): Map<Char, LetterState> {
        var currentKeyStates = emptyMap<Char, LetterState>()
        attempts.forEach { attempt ->
            val result = GuessEvaluator.evaluate(target, attempt)
            currentKeyStates = mergeKeyStates(currentKeyStates, result.feedback)
        }
        return currentKeyStates
    }
}

data class AvalancheUiState(
    val loading: Boolean = true,
    val round: Round? = null,
    val rows: List<List<Pair<Char, LetterState>>> = emptyList(),
    val currentInput: String = "",
    val finished: Boolean = false,
    val won: Boolean = false,
    val startRoundFailed: Boolean = false,
    val showHelp: Boolean = false,
    val keyStates: Map<Char, LetterState> = emptyMap()
)

sealed interface AvalancheEvent {
    data class ShowError(val message: SubmitGuessError) : AvalancheEvent
}