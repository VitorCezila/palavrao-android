package com.cezila.domain.model

data class GuessResult(
    val feedback: List<LetterFeedback>,
    val isWin: Boolean
)
