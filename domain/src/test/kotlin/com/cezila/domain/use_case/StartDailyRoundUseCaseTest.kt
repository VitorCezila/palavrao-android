package com.cezila.domain.use_case

import com.cezila.domain.Result
import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.FakeDailyWordRepository
import com.cezila.domain.use_case.daily.FetchDailyRoundUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class StartDailyRoundUseCaseTest {

    @Test
    fun `success - returns IN_PROGRESS round`() = runBlocking {
        val uc = FetchDailyRoundUseCase(
            dailyWordRepository = FakeDailyWordRepository(FakeDailyWordRepository.Mode.SUCCESS)
        )
        val res = uc()
        assertTrue(res is Result.Success)
        val round = (res as Result.Success).data
        assertEquals(RoundStatus.IN_PROGRESS, round.status)
        assertEquals("SALAO", round.target.value)
    }

    @Test
    fun `communication error - returns error`() = runBlocking {
        val uc = FetchDailyRoundUseCase(
            dailyWordRepository = FakeDailyWordRepository(FakeDailyWordRepository.Mode.COMMUNICATION_ERROR)
        )
        val res = uc()
        assertTrue(res is Result.Error)
        assertEquals(StartDailyRoundError.COMMUNICATION_ERROR, (res as Result.Error).error)
    }
}