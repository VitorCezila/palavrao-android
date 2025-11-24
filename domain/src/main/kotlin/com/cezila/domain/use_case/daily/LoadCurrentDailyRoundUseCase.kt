package com.cezila.domain.use_case.daily

import android.util.Log
import com.cezila.domain.Result
import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.daily.DailyGameProgressRepository
import kotlinx.coroutines.flow.first

class LoadCurrentDailyRoundUseCase(
    private val dailyGameProgressRepository: DailyGameProgressRepository
) {

    suspend operator fun invoke(remoteRound: Round): Result<Round, StartDailyRoundError> {

        val localRound = dailyGameProgressRepository.getDailyCurrentRound().first()

        if (
            localRound == null ||
            localRound.referenceDate == null ||
            localRound.referenceDate != remoteRound.referenceDate
        ) {
            if (localRound != null) {
                Log.d("LoadCurrentDailyRoundUseCase", "Local round exist but not up to date")
                dailyGameProgressRepository.clearDailyCurrentRound()
            }

            Log.d("LoadCurrentDailyRoundUseCase", "Local round does not exist or is not up to date")

            val newRound = Round(
                target = remoteRound.target,
                referenceDate = remoteRound.referenceDate,
                attempts = emptyList(),
                status = RoundStatus.IN_PROGRESS,
                lastResult = null
            )

            dailyGameProgressRepository.saveDailyCurrentRound(newRound)
            return Result.Success(newRound)
        }

        return Result.Success(localRound)
    }
}