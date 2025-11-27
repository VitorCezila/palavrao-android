package com.cezila.feature_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.model.Language
import com.cezila.domain.repository.SettingsRepository
import com.cezila.domain.repository.avalanche.AvalancheGameProgressRepository
import com.cezila.domain.repository.daily.DailyGameProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dailyRepository: DailyGameProgressRepository,
    private val avalancheRepository: AvalancheGameProgressRepository
) : ViewModel() {

    val currentLanguage: StateFlow<Language> = settingsRepository.getLanguage()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Language.PT)

    private val _pendingLanguageChange = MutableStateFlow<Language?>(null)
    val pendingLanguageChange: StateFlow<Language?> = _pendingLanguageChange.asStateFlow()

    fun onLanguageSelected(newLanguage: Language) {
        if (newLanguage == currentLanguage.value) return
        viewModelScope.launch {
            _pendingLanguageChange.value = newLanguage
        }
    }

    fun confirmLanguageChange() {
        val newLang = _pendingLanguageChange.value ?: return
        viewModelScope.launch {
            avalancheRepository.clearAvalancheCurrentRound()
            settingsRepository.setLanguage(newLang)
            _pendingLanguageChange.value = null
        }
    }

    fun dismissLanguageChange() {
        _pendingLanguageChange.value = null
    }

    fun onClearHistory() {
        viewModelScope.launch {
            dailyRepository.clearDailyStats()
            avalancheRepository.clearStats()
        }
    }

}