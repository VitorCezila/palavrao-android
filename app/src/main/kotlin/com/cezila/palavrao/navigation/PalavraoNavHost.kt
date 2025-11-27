package com.cezila.palavrao.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.cezila.core_navigation.PalavraoScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PalavraoNavHost(
  navHostController: NavHostController,
  modifier: Modifier = Modifier
) {
  SharedTransitionLayout {
    NavHost(
      navController = navHostController,
      startDestination = PalavraoScreen.HomeScreen,
      modifier = modifier
    ) {
      palavraoNavigation()
    }
  }
}
