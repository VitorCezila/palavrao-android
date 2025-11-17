package com.cezila.data.service

import com.cezila.data.BuildConfig
import com.cezila.data.dto.request.CreateChallengeRequest
import com.cezila.data.dto.response.CreateChallengeResponse
import com.cezila.data.dto.response.GetChallengeByCodeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IChallengeService {

    @POST("word-challenge")
    suspend fun createChallenge(@Body word: CreateChallengeRequest): CreateChallengeResponse

    @GET("word-challenge/{code}")
    suspend fun fetchChallengeByCode(
        @Path("code") code: String
    ): GetChallengeByCodeResponse

    companion object {
        val BASE_URL: String = BuildConfig.BASE_URL
    }
}