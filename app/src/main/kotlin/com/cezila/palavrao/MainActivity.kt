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
import com.cezila.feature_avalanche.AvalancheScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(

            ) {
                PalavraoTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AvalancheScreen()
                    }
                }
            }
        }
    }
}