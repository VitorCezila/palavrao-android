package com.cezila.data.dto.response

import com.cezila.domain.model.challenge.Challenge
import com.google.gson.annotations.SerializedName

data class CreateChallengeResponse(
    @SerializedName("challenge_code") val challengeCode: String,
    @SerializedName("word") val word: String,
    @SerializedName("created_at") val createdAt: String
) {
    fun CreateChallengeResponse.toModel(): Challenge {
        return Challenge(
            challengeCode = challengeCode,
            word = word
        )
    }
}