package com.cezila.palavrao.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.AppComposeNavigator
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.palavrao.navigation.PalavraoNavHost

@Composable
fun PalavraoMain(
    composeNavigator: AppComposeNavigator<PalavraoScreen>,
    snackbarHostState: SnackbarHostState
) {
    PalavraoTheme {
        val navHostController = rememberNavController()
        LaunchedEffect(Unit) {
            composeNavigator.handleNavigationCommands(navHostController)
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            PalavraoNavHost(
                navHostController = navHostController,
            )
        }
    }
}
