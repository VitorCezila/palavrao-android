package com.cezila.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LetterFeedback(
    val letter: Char,
    val state: LetterState
) : Parcelable
