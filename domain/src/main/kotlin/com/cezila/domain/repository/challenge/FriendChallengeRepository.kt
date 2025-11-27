package com.cezila.domain.repository.challenge

import com.cezila.domain.Result
import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.Word

interface FriendChallengeRepository {
    suspend fun fetchByCode(code: String): Result<Word, StartFriendRoundError>
    suspend fun createChallenge(word: Word): Result<String, CreateFriendChallengeError>
}