package com.cezila.palavrao.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.AppComposeNavigator
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.palavrao.navigation.PalavraoNavHost

@Composable
fun PalavraoMain(composeNavigator: AppComposeNavigator<PalavraoScreen>) {
    PalavraoTheme {
        val navHostController = rememberNavController()

        LaunchedEffect(Unit) {
            composeNavigator.handleNavigationCommands(navHostController)
        }

        PalavraoNavHost(navHostController = navHostController)
    }
}
