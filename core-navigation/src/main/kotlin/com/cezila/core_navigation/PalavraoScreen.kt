package com.cezila.core_navigation

import kotlinx.serialization.Serializable

sealed interface PalavraoScreen {

    @Serializable
    data object HomeScreen : PalavraoScreen

    @Serializable
    data object AvalancheScreen : PalavraoScreen

    @Serializable
    data object DailyScreen : PalavraoScreen

    @Serializable
    data object FriendChallengeScreen : PalavraoScreen

    @Serializable
    data object StatsScreen : PalavraoScreen

    @Serializable
    data object SettingsScreen : PalavraoScreen

    @Serializable
    data object AboutScreen : PalavraoScreen
}
