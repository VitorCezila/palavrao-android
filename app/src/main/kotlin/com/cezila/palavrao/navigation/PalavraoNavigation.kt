package com.cezila.palavrao.navigation

import SettingsScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.feature_avalanche.AvalancheScreen
import com.cezila.feature_challenge.menu.ChallengeMenuScreen
import com.cezila.feature_challenge.game.ChallengeScreen
import com.cezila.feature_daily.DailyScreen
import com.cezila.feature_home.HomeScreen
import com.cezila.feature_stats.StatsScreen

fun NavGraphBuilder.palavraoNavigation() {
    composable<PalavraoScreen.HomeScreen> {
        HomeScreen()
    }
    composable<PalavraoScreen.AvalancheScreen> {
        AvalancheScreen()
    }

    composable<PalavraoScreen.DailyScreen> {
        DailyScreen()
    }

    composable<PalavraoScreen.FriendChallengeHomeScreen> {
        ChallengeMenuScreen()
    }

    composable<PalavraoScreen.FriendChallengeScreen>(
        PalavraoScreen.FriendChallengeScreen.typeMap
    ) {
        ChallengeScreen()
    }

    composable<PalavraoScreen.StatsScreen> {
        StatsScreen()
    }
    composable<PalavraoScreen.SettingsScreen> {
        SettingsScreen()
    }
}
