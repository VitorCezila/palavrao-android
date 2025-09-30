package com.cezila.palavrao.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.cezila.core_navigation.PalavraoScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PalavraoNavHost(navHostController: NavHostController) {
  SharedTransitionLayout {
    NavHost(
      navController = navHostController,
      startDestination = PalavraoScreen.HomeScreen,
    ) {
      palavraoNavigation()
    }
  }
}
