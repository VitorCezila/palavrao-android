package com.cezila.palavrao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.AppComposeNavigator
import com.cezila.core_navigation.LocalComposeNavigator
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.feature_avalanche.AvalancheScreen
import com.cezila.palavrao.ui.PalavraoMain
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    internal lateinit var composeNavigator: AppComposeNavigator<PalavraoScreen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalComposeNavigator provides composeNavigator
            ) {
                PalavraoMain(composeNavigator = composeNavigator)
            }
        }
    }
}