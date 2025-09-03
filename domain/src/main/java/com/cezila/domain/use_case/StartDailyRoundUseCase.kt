package com.cezila.domain.use_case

import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.DailyWordRepository
import com.cezila.domain.result.Result
import java.time.LocalDate

class StartDailyRoundUseCase(
    private val dailyWordRepository: DailyWordRepository
) {
    suspend operator fun invoke(date: LocalDate): Result<Round, StartDailyRoundError> {
        return when (val wordRes = dailyWordRepository.fetchDaily(date)) {
            is Result.Error -> Result.Error(wordRes.error)
            is Result.Success -> Result.Success(
                Round(
                    target = wordRes.data,
                    status = RoundStatus.IN_PROGRESS
                )
            )
        }
    }
}