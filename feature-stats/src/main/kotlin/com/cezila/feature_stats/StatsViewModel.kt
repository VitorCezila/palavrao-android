package com.cezila.feature_stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.model.avalanche.AvalancheStats
import com.cezila.domain.use_case.GetAvalancheStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getAvalancheStats: GetAvalancheStatsUseCase
) : ViewModel() {

    val uiState: StateFlow<StatsUiState> = getAvalancheStats()
        .map { StatsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatsUiState()
        )
}

data class StatsUiState(
    val avalancheStats: AvalancheStats = AvalancheStats()
)