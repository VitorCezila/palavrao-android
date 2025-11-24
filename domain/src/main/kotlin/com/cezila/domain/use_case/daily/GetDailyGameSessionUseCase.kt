package com.cezila.domain.use_case.daily

import android.util.Log
import com.cezila.domain.Result
import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.TimeRepository
import com.cezila.domain.repository.daily.DailyGameProgressRepository
import com.cezila.domain.repository.daily.DailyWordRepository
import com.cezila.domain.utils.DateUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDailyGameSessionUseCase @Inject constructor(
    private val timeRepository: TimeRepository,
    private val localRepository: DailyGameProgressRepository,
    private val remoteRepository: DailyWordRepository
) {

    suspend operator fun invoke(): Result<Round, StartDailyRoundError> {
        val currentTimeMillis = timeRepository.getCurrentTimeMillis()

        if (currentTimeMillis == null) {
            Log.e("GetDailyGameSessionUseCase", "currentTimeMillis is null")
            return Result.Error(StartDailyRoundError.COMMUNICATION_ERROR)
        }

        val todayDateString = DateUtils.millisToDayString(currentTimeMillis)

        val localRound = localRepository.getDailyCurrentRound().first()

        if (localRound != null && localRound.referenceDate == todayDateString) {
            return Result.Success(localRound)
        }

        return when (val remoteResult = remoteRepository.fetchDaily()) {
            is Result.Error -> {
                Result.Error(remoteResult.error)
            }
            is Result.Success -> {
                val dailyInfo = remoteResult.data

                val newRound = Round(
                    target = dailyInfo.word,
                    referenceDate = dailyInfo.referenceDate,
                    attempts = emptyList(),
                    status = RoundStatus.IN_PROGRESS,
                    lastResult = null
                )

                localRepository.clearDailyCurrentRound()
                localRepository.saveDailyCurrentRound(newRound)

                Result.Success(newRound)
            }
        }
    }
}