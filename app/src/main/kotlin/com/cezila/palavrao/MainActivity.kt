package com.cezila.palavrao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.cezila.core_designsystem.LocalSnackbarHostState
import com.cezila.core_navigation.AppComposeNavigator
import com.cezila.core_navigation.LocalComposeNavigator
import com.cezila.core_navigation.PalavraoScreen
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
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalComposeNavigator provides composeNavigator,
                LocalSnackbarHostState provides snackbarHostState
            ) {
                PalavraoMain(
                    composeNavigator = composeNavigator,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}