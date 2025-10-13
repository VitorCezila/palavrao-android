package com.cezila.palavrao.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.feature_avalanche.AvalancheScreen
import com.cezila.feature_home.HomeScreen

fun NavGraphBuilder.palavraoNavigation() {
    composable<PalavraoScreen.HomeScreen> {
        HomeScreen()
    }
    composable<PalavraoScreen.AvalancheScreen> {
        AvalancheScreen()
    }

    composable<PalavraoScreen.DailyScreen> {
        PlaceholderScreen("Palavra do dia — em breve")
    }

    composable<PalavraoScreen.FriendChallengeScreen> {
        PlaceholderScreen("Desafio — em breve")
    }

    composable<PalavraoScreen.StatsScreen> {
        PlaceholderScreen("Estatística — em breve")
    }
    composable<PalavraoScreen.SettingsScreen> {
        PlaceholderScreen("Configuração — em breve")
    }
    composable<PalavraoScreen.AboutScreen> {
        PlaceholderScreen("Sobre nós — em breve")
    }
}
