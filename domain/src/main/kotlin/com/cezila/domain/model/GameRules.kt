package com.cezila.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GameRules(
    val wordLength: Int = 5,
    val maxAttempts: Int = 6
) : Parcelable
