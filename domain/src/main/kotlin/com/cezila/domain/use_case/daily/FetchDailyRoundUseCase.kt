package com.cezila.domain.use_case.daily

import com.cezila.domain.Result
import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.model.daily.DailyWordInfo
import com.cezila.domain.repository.daily.DailyWordRepository

class FetchDailyRoundUseCase(
    private val dailyWordRepository: DailyWordRepository
) {
    suspend operator fun invoke(): Result<DailyRound, StartDailyRoundError> {
        return when (val wordRes = dailyWordRepository.fetchDaily()) {
            is Result.Error -> Result.Error(wordRes.error)
            is Result.Success -> Result.Success(
                DailyRound(
                    round = Round(
                        target = wordRes.data.word,
                        status = RoundStatus.IN_PROGRESS
                    ),
                    dailyInfo = wordRes.data
                )
            )
        }
    }
}

data class DailyRound(
    val round: Round,
    val dailyInfo: DailyWordInfo
)