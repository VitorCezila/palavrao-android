package com.cezila.data.repository.challenge

import android.util.Log
import com.cezila.data.dto.request.CreateChallengeRequest
import com.cezila.data.service.IChallengeService
import com.cezila.domain.Result
import com.cezila.domain.error.CreateFriendChallengeError
import com.cezila.domain.error.StartFriendRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.repository.challenge.FriendChallengeRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class FriendChallengeRemoteRepository @Inject constructor(
    private val api: IChallengeService
) : FriendChallengeRepository {

    override suspend fun fetchByCode(code: String): Result<Word, StartFriendRoundError> {
        return try {
            val response = api.fetchChallengeByCode(code)
            Log.d(TAG, "Response: ${response.word}")
            return Result.Success(Word.w(response.word))
        } catch (e: IOException) {
            Log.e(TAG, "Error starting challenge word (IO): $e")
            Result.Error(StartFriendRoundError.COMMUNICATION_ERROR)
        } catch (e: HttpException) {
            Log.e(TAG, "Error starting challenge (HTTP): $e")
            if (e.code() == 404) {
                return Result.Error(StartFriendRoundError.NOT_FOUND)
            }
            Result.Error(StartFriendRoundError.COMMUNICATION_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting challenge (Generic): $e")
            Result.Error(StartFriendRoundError.COMMUNICATION_ERROR)
        }
    }

    override suspend fun createChallenge(word: Word): Result<String, CreateFriendChallengeError> {
        return try {
            val response = api.createChallenge(CreateChallengeRequest(word.value))
            Log.d(TAG, "Response: ${response.challengeCode}")
            return Result.Success(response.challengeCode)
        } catch (e: IOException) {
            Log.e(TAG, "Error creating challenge word (IO): $e")
            Result.Error(CreateFriendChallengeError.COMMUNICATION_ERROR)
        } catch (e: HttpException) {
            Log.e(TAG, "Error creating challenge (HTTP): $e")
            Result.Error(CreateFriendChallengeError.COMMUNICATION_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating challenge (Generic): $e")
            Result.Error(CreateFriendChallengeError.COMMUNICATION_ERROR)
        }
    }

    private companion object {
        const val TAG = "FriendChallengeRepository"
    }
}