package com.cezila.domain.use_case.daily

import com.cezila.domain.model.daily.DailyStats
import com.cezila.domain.repository.daily.DailyGameProgressRepository
import kotlinx.coroutines.flow.Flow

class GetDailyStatsUseCase(
    private val repository: DailyGameProgressRepository
) {

    operator fun invoke(): Flow<DailyStats> {
        return repository.getDailyStats()
    }

}