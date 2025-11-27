package com.cezila.data.repository

import android.content.Context
import android.util.Log
import com.cezila.domain.repository.TimeRepository
import com.google.android.gms.time.TrustedTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleTimeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : TimeRepository {

    override suspend fun getCurrentTimeMillis(): Long? = withContext(Dispatchers.IO) {
        val maxAttempts = 2

        repeat(maxAttempts) { attempt ->
            try {
                val client = TrustedTime.createClient(context).await()
                val time = client.computeCurrentUnixEpochMillis()

                if (time != null) {
                    Log.i("GoogleTimeRepository", "Time fetched successfully $time")
                    return@withContext time
                } else {
                    Log.w(
                        "GoogleTimeRepository",
                        "Attempt ${attempt + 1}: TrustedTime client ready, but time is null"
                    )
                }
            } catch (e: Exception) {
                Log.e("GoogleTimeRepository", "Attempt ${attempt + 1} failed: ${e.message}")
            }
            if (attempt < maxAttempts - 1) {
                delay(500)
            }
        }
        return@withContext null
    }
}