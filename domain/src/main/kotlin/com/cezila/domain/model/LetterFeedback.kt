package com.cezila.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LetterFeedback(
    val letter: Char,
    val state: LetterState
)
