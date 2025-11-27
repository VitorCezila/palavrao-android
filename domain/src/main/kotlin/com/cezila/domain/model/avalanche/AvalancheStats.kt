package com.cezila.domain.model.avalanche

import kotlinx.serialization.Serializable

@Serializable
data class AvalancheStats(
    val gamesPlayed: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
)
