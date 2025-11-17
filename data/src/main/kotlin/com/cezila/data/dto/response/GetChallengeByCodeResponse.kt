package com.cezila.data.dto.response

import com.google.gson.annotations.SerializedName

data class GetChallengeByCodeResponse(
    @SerializedName("challenge_code") val challengeCode: String,
    @SerializedName("word") val word: String,
    @SerializedName("created_at") val createdAt: String
)
