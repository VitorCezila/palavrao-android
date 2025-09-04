package com.cezila.domain.use_case

import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.FriendChallengeRepository
import com.cezila.domain.Result

class StartFriendRoundUseCase(
    private val friendChallengeRepository: FriendChallengeRepository
) {
    suspend operator fun invoke(code: String): Result<Round, StartFriendRoundError> {
        return when (val wordRes = friendChallengeRepository.fetchByCode(code)) {
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