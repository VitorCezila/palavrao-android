package com.cezila.domain.use_case

import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.FakeFriendChallengeRepository
import com.cezila.domain.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class StartFriendRoundUseCaseTest {

    @Test
    fun `success - returns IN_PROGRESS round`() = runBlocking {
        val uc = StartFriendRoundUseCase(
            friendChallengeRepository = FakeFriendChallengeRepository(mode = FakeFriendChallengeRepository.Mode.SUCCESS)
        )
        val res = uc("ABC123")
        assertTrue(res is Result.Success)
        val round = (res as Result.Success).data
        assertEquals(RoundStatus.IN_PROGRESS, round.status)
        assertEquals("SALTO", round.target.value)
    }

    @Test
    fun `not found - returns CHALLENGE_NOT_FOUND`() = runBlocking {
        val uc = StartFriendRoundUseCase(
            friendChallengeRepository = FakeFriendChallengeRepository(mode = FakeFriendChallengeRepository.Mode.NOT_FOUND)
        )
        val res = uc("ZZZ999")
        assertTrue(res is Result.Error)
        assertEquals(StartFriendRoundError.NOT_FOUND, (res as Result.Error).error)
    }

    @Test
    fun `network error - returns NETWORK_ERROR`() = runBlocking {
        val uc = StartFriendRoundUseCase(
            friendChallengeRepository = FakeFriendChallengeRepository(mode = FakeFriendChallengeRepository.Mode.COMMUNICATION_ERROR)
        )
        val res = uc("ANY")
        assertTrue(res is Result.Error)
        assertEquals(StartFriendRoundError.COMMUNICATION_ERROR, (res as Result.Error).error)
    }

}