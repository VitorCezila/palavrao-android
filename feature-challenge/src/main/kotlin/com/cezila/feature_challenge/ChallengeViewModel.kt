package com.cezila.feature_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.Result
import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.use_case.challenge.CreateFriendChallengeUseCase
import com.cezila.domain.use_case.challenge.StartFriendRoundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val createFriendChallengeUseCase: CreateFriendChallengeUseCase,
    private val startFriendRoundUseCase: StartFriendRoundUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ChallengeNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: ChallengeEvent) {
        when (event) {
            is ChallengeEvent.OnCreateWordChanged -> {
                val newWord = event.word.uppercase().take(5)
                _uiState.update { it.copy(createWord = newWord) }
            }
            is ChallengeEvent.OnJoinCodeChanged -> {
                val newCode = event.code.uppercase().take(6)
                _uiState.update { it.copy(joinCode = newCode) }
            }
            is ChallengeEvent.OnCreateChallengeClicked -> createChallenge()
            is ChallengeEvent.OnStartChallengeClicked -> startChallenge()
            is ChallengeEvent.OnErrorDialogDismissed -> {
                _uiState.update { it.copy(createError = null, startError = null) }
            }
        }
    }

    fun toggleHelp(show: Boolean) {
        _uiState.update { it.copy(showHelp = show) }
    }

    private fun createChallenge() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingChallenge = true) }

            val word = _uiState.value.createWord
            val result = createFriendChallengeUseCase(word)

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreatingChallenge = false,
                            generatedCode = result.data,
                            createError = null,
                            createWord = ""
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreatingChallenge = false,
                            generatedCode = null,
                            createError = result.error
                        )
                    }
                }
            }
        }
    }

    private fun startChallenge() {
        viewModelScope.launch {
            _uiState.update { it.copy(isStartingChallenge = true) }

            val code = _uiState.value.joinCode
            val result = startFriendRoundUseCase(code)

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isStartingChallenge = false,
                            startError = null
                        )
                    }
                    _navigationEvent.emit(ChallengeNavigation.NavigateToGame(result.data))
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isStartingChallenge = false,
                            startError = result.error
                        )
                    }
                }
            }
        }
    }
}

data class ChallengeUiState(
    val createWord: String = "",
    val generatedCode: String? = null,
    val isCreatingChallenge: Boolean = false,
    val createError: CreateFriendChallengeError? = null,
    val joinCode: String = "",
    val isStartingChallenge: Boolean = false,
    val startError: StartFriendRoundError? = null,
    val showHelp: Boolean = false
)

sealed interface ChallengeEvent {
    data class OnCreateWordChanged(val word: String) : ChallengeEvent
    data class OnJoinCodeChanged(val code: String) : ChallengeEvent
    object OnCreateChallengeClicked : ChallengeEvent
    object OnStartChallengeClicked : ChallengeEvent
    object OnErrorDialogDismissed : ChallengeEvent
}

sealed interface ChallengeNavigation {
    data class NavigateToGame(val round: Round) : ChallengeNavigation
}