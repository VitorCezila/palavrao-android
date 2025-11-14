package com.cezila.domain.use_case.daily

import com.cezila.domain.repository.daily.DailyGameProgressRepository

class UpdateDailyGameResultUseCase(
    private val dailyGameProgressRepository: DailyGameProgressRepository
) {
    suspend operator fun invoke(gameWon: Boolean) {
        dailyGameProgressRepository.updateDailyStats(gameWon)
    }
}