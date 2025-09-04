package com.cezila.domain.use_case

import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.repository.FakeDictionaryRepository
import com.cezila.domain.repository.FakeFriendChallengeRepository
import com.cezila.domain.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateFriendChallengeUseCaseTest {

    @Test
    fun `invalid length - returns INVALID_LENGTH`() = runBlocking {
        val uc = CreateFriendChallengeUseCase(
            dictionaryRepository = FakeDictionaryRepository(setOf("SALTO")),
            friendChallengeRepository = FakeFriendChallengeRepository()
        )
        val res = uc("SAL")
        assertTrue(res is Result.Error)
        assertEquals(CreateFriendChallengeError.INVALID_LENGTH, (res as Result.Error).error)
    }

    @Test
    fun `not in dictionary - returns INVALID_WORD`() = runBlocking {
        val uc = CreateFriendChallengeUseCase(
            dictionaryRepository = FakeDictionaryRepository(setOf("SALTO")),
            friendChallengeRepository = FakeFriendChallengeRepository()
        )
        val res = uc("SALAO")
        assertTrue(res is Result.Error)
        assertEquals(CreateFriendChallengeError.INVALID_WORD, (res as Result.Error).error)
    }

    @Test
    fun `communication error - returns COMMUNICATION_ERROR`() = runBlocking {
        val uc = CreateFriendChallengeUseCase(
            dictionaryRepository = FakeDictionaryRepository(setOf("SALTO")),
            friendChallengeRepository = FakeFriendChallengeRepository(mode = FakeFriendChallengeRepository.Mode.COMMUNICATION_ERROR)
        )
        val res = uc("SALTO")
        assertTrue(res is Result.Error)
        assertEquals(CreateFriendChallengeError.COMMUNICATION_ERROR, (res as Result.Error).error)
    }

    @Test
    fun `success - returns created code`() = runBlocking {
        val uc = CreateFriendChallengeUseCase(
            dictionaryRepository = FakeDictionaryRepository(setOf("SALTO")),
            friendChallengeRepository = FakeFriendChallengeRepository(codeToReturn = "ABC123")
        )
        val res = uc("SALTO")
        assertTrue(res is Result.Success)
        assertEquals("ABC123", (res as Result.Success).data)
    }

}