package com.cezila.domain.model.daily

import kotlinx.serialization.Serializable

@Serializable
data class DailyStats(
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0
)