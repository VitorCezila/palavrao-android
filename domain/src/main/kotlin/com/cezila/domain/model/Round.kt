package com.cezila.domain.model

data class Round(
    val target: Word,
    val attempts: List<Word> = emptyList(),
    val status: RoundStatus = RoundStatus.IN_PROGRESS,
    val lastResult: GuessResult? = null
) {
    val remainingAttempts: Int get() = GameRules().maxAttempts - attempts.size
    val finished: Boolean get() = status != RoundStatus.IN_PROGRESS
}