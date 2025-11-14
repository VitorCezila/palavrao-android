package com.cezila.domain.use_case

import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.repository.FakeAvalancheRepository
import com.cezila.domain.Result
import com.cezila.domain.use_case.avalanche.StartAvalancheRoundUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class StartAvalancheRoundUseCaseTest {

    @Test
    fun `success - returns IN_PROGRESS round`() = runBlocking {
        val uc = StartAvalancheRoundUseCase(
            avalancheRepository = FakeAvalancheRepository(FakeAvalancheRepository.Mode.SUCCESS)
        )
        val res = uc()
        assertTrue(res is Result.Success)
        val round = (res as Result.Success).data
        assertEquals(RoundStatus.IN_PROGRESS, round.status)
        assertEquals("SALTO", round.target.value)
    }

    @Test
    fun `communication error - returns error`() = runBlocking {
        val uc = StartAvalancheRoundUseCase(
            avalancheRepository = FakeAvalancheRepository(FakeAvalancheRepository.Mode.COMMUNICATION_ERROR)
        )
        val res = uc()
        assertTrue(res is Result.Error)
        assertEquals(StartAvalancheRoundError.COMMUNICATION_ERROR, (res as Result.Error).error)
    }

}