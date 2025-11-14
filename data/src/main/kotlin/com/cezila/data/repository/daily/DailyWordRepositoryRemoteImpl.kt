package com.cezila.data.repository.daily

import android.util.Log
import com.cezila.data.dto.response.toModel
import com.cezila.data.service.IDailyService
import com.cezila.domain.Result
import com.cezila.domain.error.StartDailyRoundError
import com.cezila.domain.model.daily.DailyWordInfo
import com.cezila.domain.repository.daily.DailyWordRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class DailyWordRepositoryRemoteImpl @Inject constructor(
    private val service: IDailyService
) : DailyWordRepository {

    override suspend fun fetchDaily(): Result<DailyWordInfo, StartDailyRoundError> {
        return try {
            val response = service.fetchDailyWord()
            Log.d(TAG, "Response: ${response.dailyWord}")

            Result.Success(response.toModel())
        } catch (e: IOException) {
            Log.e(TAG, "Error fetching word (IO): $e")
            Result.Error(StartDailyRoundError.COMMUNICATION_ERROR)
        } catch (e: HttpException) {
            Log.e(TAG, "Error fetching word (HTTP): $e")
            Result.Error(StartDailyRoundError.COMMUNICATION_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching word (Generic): $e")
            Result.Error(StartDailyRoundError.COMMUNICATION_ERROR)
        }
    }

    private companion object {
        const val TAG = "DailyRepositoryRemote"
    }
}