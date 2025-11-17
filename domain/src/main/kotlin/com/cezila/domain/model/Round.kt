package com.cezila.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Round(
    val target: Word,
    val attempts: List<Word> = emptyList(),
    val status: RoundStatus = RoundStatus.IN_PROGRESS,
    val lastResult: GuessResult? = null,
    val referenceDate: String? = null
): Parcelable