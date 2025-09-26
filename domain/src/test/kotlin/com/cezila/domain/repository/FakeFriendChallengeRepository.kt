package com.cezila.domain.repository

import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.Result

class FakeFriendChallengeRepository(
    private val codeToReturn: String = "9F2K6X",
    private val mode: Mode = Mode.SUCCESS
) : FriendChallengeRepository {

    private fun w(s: String): Word =
        when (val r = Word.of(s)) {
            is Result.Success -> r.data
            is Result.Error -> error("Word.of('$s') failed with ${r.error}")
        }

    enum class Mode { SUCCESS, NOT_FOUND, COMMUNICATION_ERROR }

    override suspend fun fetchByCode(code: String): Result<Word, StartFriendRoundError> =
        when (mode) {
            Mode.SUCCESS -> Result.Success(w("SALTO"))
            Mode.NOT_FOUND -> Result.Error(StartFriendRoundError.NOT_FOUND)
            Mode.COMMUNICATION_ERROR -> Result.Error(StartFriendRoundError.COMMUNICATION_ERROR)
        }

    override suspend fun createChallenge(word: Word): Result<String, CreateFriendChallengeError> {
        return if (mode == Mode.COMMUNICATION_ERROR) Result.Error(CreateFriendChallengeError.COMMUNICATION_ERROR)
        else Result.Success(codeToReturn)
    }
}