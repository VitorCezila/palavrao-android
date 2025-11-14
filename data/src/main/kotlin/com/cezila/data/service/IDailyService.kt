package com.cezila.data.service

import com.cezila.data.BuildConfig
import com.cezila.data.dto.response.DailyWordResponse
import retrofit2.http.GET

interface IDailyService {

    @GET("daily-word")
    suspend fun fetchDailyWord(): DailyWordResponse

    companion object {
        val BASE_URL: String = BuildConfig.BASE_URL
    }
}