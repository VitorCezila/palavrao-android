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
}
