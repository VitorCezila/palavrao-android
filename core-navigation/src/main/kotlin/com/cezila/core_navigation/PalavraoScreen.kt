package com.cezila.core_navigation

import com.cezila.domain.model.Round
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface PalavraoScreen {

    @Serializable
    data object HomeScreen : PalavraoScreen

    @Serializable
    data object AvalancheScreen : PalavraoScreen

    @Serializable
    data object DailyScreen : PalavraoScreen

    @Serializable
    data object FriendChallengeHomeScreen : PalavraoScreen

    @Serializable
    data class FriendChallengeScreen(
        val round: Round,
        val code: String
    ) : PalavraoScreen {
        companion object {
            val typeMap = mapOf(typeOf<Round>() to RoundType)
        }
    }

    @Serializable
    data object StatsScreen : PalavraoScreen

    @Serializable
    data object SettingsScreen : PalavraoScreen

    @Serializable
    data object AboutScreen : PalavraoScreen
}
