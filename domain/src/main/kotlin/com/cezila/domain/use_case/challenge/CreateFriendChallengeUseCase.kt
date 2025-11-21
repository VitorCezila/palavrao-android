package com.cezila.domain.use_case.challenge

import com.cezila.domain.Result
import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.WordError
import com.cezila.domain.model.Word
import com.cezila.domain.repository.DictionaryRepository
import com.cezila.domain.repository.FriendChallengeRepository

class CreateFriendChallengeUseCase(
    private val dictionaryRepository: DictionaryRepository,
    private val friendChallengeRepository: FriendChallengeRepository
) {
    suspend operator fun invoke(rawWord: String): Result<String, CreateFriendChallengeError> {
        val word = when (val r = Word.of(rawWord, 5)) {
            is Result.Success -> r.data
            is Result.Error -> return when (r.error) {
                WordError.INVALID_LENGTH -> Result.Error(CreateFriendChallengeError.INVALID_LENGTH)
                WordError.INVALID_CHARS -> Result.Error(CreateFriendChallengeError.INVALID_CHARS)
            }
        }

        if (!dictionaryRepository.isWordValid(word)) {
            return Result.Error(CreateFriendChallengeError.INVALID_WORD)
        }

        return friendChallengeRepository.createChallenge(word)
    }
}