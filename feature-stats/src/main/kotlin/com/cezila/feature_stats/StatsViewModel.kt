package com.cezila.feature_stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cezila.domain.model.avalanche.AvalancheStats
import com.cezila.domain.model.daily.DailyStats
import com.cezila.domain.use_case.avalanche.GetAvalancheStatsUseCase
import com.cezila.domain.use_case.daily.GetDailyStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    getAvalancheStats: GetAvalancheStatsUseCase,
    getDailyStats: GetDailyStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatsUiState())
    val state: StateFlow<StatsUiState> = _state.asStateFlow()

    init {
        getDailyStats()
            .onEach { dailyStats ->
                _state.update { currentState ->
                    currentState.copy(
                        dailyStats = dailyStats
                    )
                }
            }
            .launchIn(viewModelScope)

        getAvalancheStats()
            .onEach { avalancheStats ->
                _state.update { currentState ->
                    currentState.copy(
                        avalancheStats = avalancheStats
                    )
                }
            }
            .launchIn(viewModelScope)
    }

}

data class StatsUiState(
    val avalancheStats: AvalancheStats = AvalancheStats(),
    val dailyStats: DailyStats = DailyStats()
)