package com.cezila.domain.repository

import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.result.Result

interface FriendChallengeRepository {
    suspend fun fetchByCode(code: String): Result<Word, StartFriendRoundError>
    suspend fun createChallenge(word: Word): Result<String, CreateFriendChallengeError>
}