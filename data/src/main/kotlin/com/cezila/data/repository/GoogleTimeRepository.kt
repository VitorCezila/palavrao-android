package com.cezila.data.repository

import android.content.Context
import android.util.Log
import com.cezila.domain.repository.TimeRepository
import com.google.android.gms.time.TrustedTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleTimeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : TimeRepository {

    override suspend fun getCurrentTimeMillis(): Long? = withContext(Dispatchers.IO) {
        try {
            val client = TrustedTime.createClient(context).await()

            val time = client.computeCurrentUnixEpochMillis()

            if (time == null) {
                Log.e("GoogleTimeRepository", "TrustedTime client ready, but time is null")
            }

            return@withContext time
        } catch (e: Exception) {
            Log.e("GoogleTimeRepository", "Failed to initialize TrustedTime: ${e.message}")
            return@withContext null
        }
    }
}