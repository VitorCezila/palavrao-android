package com.cezila.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Round(
    val target: Word,
    val attempts: List<Word> = emptyList(),
    val status: RoundStatus = RoundStatus.IN_PROGRESS,
    val lastResult: GuessResult? = null,
    val referenceDate: String? = null
)