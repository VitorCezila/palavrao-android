package com.cezila.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GuessResult(
    val feedback: List<LetterFeedback>,
    val isWin: Boolean
) : Parcelable
