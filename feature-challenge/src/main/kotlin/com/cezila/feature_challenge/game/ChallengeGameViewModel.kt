package com.cezila.feature_challenge.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.Result
import com.cezila.domain.error.SubmitGuessError
import com.cezila.domain.model.LetterFeedback
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.use_case.SubmitGuessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeGameViewModel @Inject constructor(
    private val submitGuess: SubmitGuessUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeGameUiState())
    val uiState: StateFlow<ChallengeGameUiState> = _uiState.asStateFlow()

    private val _events = Channel<ChallengeGameEvent>()
    val events = _events.receiveAsFlow()

    init {
        savedStateHandle.getStateFlow<Round?>("round", null)
            .onEach { initialRound ->
                if (initialRound != null && _uiState.value.round == null) {
                    _uiState.update {
                        it.copy(
                            round = initialRound,
                            rows = emptyList(),
                            currentInput = List(5) { null },
                            currentCursor = 0,
                            keyStates = emptyMap(),
                            finished = false,
                            won = false
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
        savedStateHandle.getStateFlow("code", initialValue = "")
            .onEach { code ->
                if (code.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            challengeCode = code
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onTileSelected(index: Int) {
        val s = _uiState.value
        if (s.finished || s.round == null) return
        if (index in 0..4) {
            _uiState.update { it.copy(currentCursor = index) }
        }
    }

    fun onKey(ch: Char) {
        val s = _uiState.value
        if (s.finished || s.round == null) return
        val cursor = s.currentCursor
        if (cursor > 4) return

        val newInput = s.currentInput.toMutableList()
        newInput[cursor] = ch

        val nextCursor = (cursor + 1).coerceAtMost(4)

        _uiState.update {
            it.copy(
                currentInput = newInput,
                currentCursor = nextCursor
            )
        }
    }

    fun onBackspace() {
        val s = _uiState.value
        if (s.finished) return
        val cursor = s.currentCursor
        val newInput = s.currentInput.toMutableList()

        if (cursor == 0 && newInput[0] == null) return

        if (newInput[cursor] != null) {
            newInput[cursor] = null
            _uiState.update { it.copy(currentInput = newInput) }
        } else if (cursor > 0) {
            newInput[cursor - 1] = null
            _uiState.update {
                it.copy(
                    currentInput = newInput,
                    currentCursor = cursor - 1
                )
            }
        }
    }

    fun onEnter() {
        val s = _uiState.value
        val round = s.round ?: return
        if (s.currentInput.any { it == null }) {
            return
        }

        val guessString = s.currentInput.filterNotNull().joinToString("")
        viewModelScope.launch {
            when (val res = submitGuess(round, guessString)) {
                is Result.Error -> {
                    _events.send(ChallengeGameEvent.ShowGuessError(res.error))
                }

                is Result.Success -> {
                    val out = res.data
                    val updatedRound = out.round
                    val row = out.result.feedback.map { it.letter to it.state }

                    _uiState.update {
                        it.copy(
                            round = updatedRound,
                            rows = it.rows + listOf(row),
                            currentInput = List(5) { null },
                            currentCursor = 0,
                            finished = updatedRound.status != RoundStatus.IN_PROGRESS,
                            won = updatedRound.status == RoundStatus.WON,
                            keyStates = mergeKeyStates(it.keyStates, out.result.feedback)
                        )
                    }
                }
            }
        }
    }

    fun toggleHelp(show: Boolean) {
        _uiState.update { it.copy(showHelp = show) }
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

    fun showEndDialog() {
        _uiState.update { it.copy(isEndDialogVisible = true) }
    }

    fun hideEndDialog() {
        _uiState.update { it.copy(isEndDialogVisible = false) }
    }
}

data class ChallengeGameUiState(
    val round: Round? = null,
    val rows: List<List<Pair<Char, LetterState>>> = emptyList(),
    val currentInput: List<Char?> = List(5) { null },
    val currentCursor: Int = 0,
    val finished: Boolean = false,
    val won: Boolean = false,
    val showHelp: Boolean = false,
    val keyStates: Map<Char, LetterState> = emptyMap(),
    val challengeCode: String = "",
    val isEndDialogVisible: Boolean = true
)

sealed interface ChallengeGameEvent {
    data class ShowGuessError(val message: SubmitGuessError) : ChallengeGameEvent
}