package com.cezila.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GuessResult(
    val feedback: List<LetterFeedback>,
    val isWin: Boolean
)
