package com.cezila.domain.use_case.daily

import com.cezila.domain.model.Round
import com.cezila.domain.repository.daily.DailyGameProgressRepository

class SaveDailyRoundUseCase(
    private val dailyGameProgressRepository: DailyGameProgressRepository
) {
    suspend operator fun invoke(round: Round) =
        dailyGameProgressRepository.saveDailyCurrentRound(round = round)
}