package com.cezila.data.dto.response

import com.cezila.domain.model.Word
import com.cezila.domain.model.daily.DailyWordInfo
import com.google.gson.annotations.SerializedName


data class DailyWordResponse(
    @SerializedName("daily_word") val dailyWord: String,
    @SerializedName("reference_date") val referenceDate: String
)

fun DailyWordResponse.toModel(): DailyWordInfo {
    return DailyWordInfo(
        word = Word.Companion.w(dailyWord),
        referenceDate = referenceDate
    )
}

