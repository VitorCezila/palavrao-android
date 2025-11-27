package com.cezila.feature_avalanche

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.Result
import com.cezila.domain.error.SubmitGuessError
import com.cezila.domain.model.LetterFeedback
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.model.Word
import com.cezila.domain.repository.SettingsRepository
import com.cezila.domain.rules.GuessEvaluator
import com.cezila.domain.use_case.SubmitGuessUseCase
import com.cezila.domain.use_case.avalanche.GetAvalancheStatsUseCase
import com.cezila.domain.use_case.avalanche.LoadCurrentAvalancheRoundUseCase
import com.cezila.domain.use_case.avalanche.SaveAvalancheRoundUseCase
import com.cezila.domain.use_case.avalanche.StartAvalancheRoundUseCase
import com.cezila.domain.use_case.avalanche.UpdateAvalancheGameResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvalancheViewModel @Inject constructor(
    private val startRound: StartAvalancheRoundUseCase,
    private val submitGuess: SubmitGuessUseCase,
    private val loadCurrentRound: LoadCurrentAvalancheRoundUseCase,
    private val saveRound: SaveAvalancheRoundUseCase,
    private val updateGameResult: UpdateAvalancheGameResultUseCase,
    getAvalancheStats: GetAvalancheStatsUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AvalancheUiState())
    val state: StateFlow<AvalancheUiState> = _state.asStateFlow()

    private val _events = Channel<AvalancheEvent>()
    val events = _events.receiveAsFlow()

    init {
        getAvalancheStats()
            .onEach { stats ->
                _state.update { it.copy(currentStreak = stats.currentStreak) }
            }
            .launchIn(viewModelScope)
    }

    fun start() {
        _state.update {
            it.copy(
                loading = true,
                startRoundFailed = false,
                rows = emptyList(),
                currentInput = List(5) { null },
                currentCursor = 0,
                keyStates = emptyMap(),
                finished = false,
                isEndDialogVisible = true
            )
        }
        viewModelScope.launch {
            val savedRound = loadCurrentRound().first()
            if (savedRound != null) {
                val rows = savedRound.attempts.map { attempt ->
                    val result = GuessEvaluator.evaluate(attempt, savedRound.target)
                    result.feedback.map { it.letter to it.state }
                }
                val keyStates = mergeKeyStatesFromAttempts(savedRound.attempts, savedRound.target)
                _state.update {
                    it.copy(
                        loading = false,
                        round = savedRound,
                        rows = rows,
                        keyStates = keyStates,
                        currentInput = List(5) { null },
                        currentCursor = 0,
                        finished = savedRound.status != RoundStatus.IN_PROGRESS,
                        won = savedRound.status == RoundStatus.WON
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
                            currentInput = List(5) { null },
                            currentCursor = 0,
                            keyStates = emptyMap()
                        )
                    }
                }
            }
        }
    }

    fun onTileSelected(index: Int) {
        val s = _state.value
        if (s.finished || s.round == null) return
        if (index in 0..4) {
            _state.update { it.copy(currentCursor = index) }
        }
    }

    fun onKey(ch: Char) {
        val s = _state.value
        if (s.finished || s.round == null) return
        val cursor = s.currentCursor
        if (cursor > 4) return

        val newInput = s.currentInput.toMutableList()
        newInput[cursor] = ch

        val nextCursor = (cursor + 1).coerceAtMost(4)

        _state.update {
            it.copy(
                currentInput = newInput,
                currentCursor = nextCursor
            )
        }
    }

    fun onBackspace() {
        val s = _state.value
        if (s.finished) return
        val cursor = s.currentCursor
        val newInput = s.currentInput.toMutableList()

        if (cursor == 0 && newInput[0] == null) return

        if (newInput[cursor] != null) {
            newInput[cursor] = null
            _state.update { it.copy(currentInput = newInput) }
        }

        else if (cursor > 0) {
            newInput[cursor - 1] = null
            _state.update {
                it.copy(
                    currentInput = newInput,
                    currentCursor = cursor - 1
                )
            }
        }
    }

    fun onEnter() {
        val s = _state.value
        val round = s.round ?: return
        if (s.currentInput.any { it == null }) {
            return
        }

        val guessString = s.currentInput.filterNotNull().joinToString("")
        viewModelScope.launch {
            val currentLang = settingsRepository.getLanguage().first()
            when (val res = submitGuess(round, guessString, currentLang)) {
                is Result.Error -> {
                    _events.send(AvalancheEvent.ShowGuessError(res.error))
                }

                is Result.Success -> {
                    val out = res.data
                    val updatedRound = out.round
                    val row = out.result.feedback.map { it.letter to it.state }
                    _state.update {
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

    fun playAgain() {
        start()
    }

    fun showEndDialog() {
        _state.update { it.copy(isEndDialogVisible = true) }
    }

    fun hideEndDialog() {
        _state.update { it.copy(isEndDialogVisible = false) }
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
            val result = GuessEvaluator.evaluate(attempt, target)
            currentKeyStates = mergeKeyStates(currentKeyStates, result.feedback)
        }
        return currentKeyStates
    }
}

data class AvalancheUiState(
    val loading: Boolean = true,
    val round: Round? = null,
    val rows: List<List<Pair<Char, LetterState>>> = emptyList(),
    val currentInput: List<Char?> = List(5) { null },
    val currentCursor: Int = 0,
    val finished: Boolean = false,
    val won: Boolean = false,
    val startRoundFailed: Boolean = false,
    val showHelp: Boolean = false,
    val keyStates: Map<Char, LetterState> = emptyMap(),
    val currentStreak: Int = 0,
    val isEndDialogVisible: Boolean = true
)

sealed interface AvalancheEvent {
    data class ShowGuessError(val message: SubmitGuessError) : AvalancheEvent
}