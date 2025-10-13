package com.cezila.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.core_navigation.currentComposeNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val nav = currentComposeNavigator

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Ajuda")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            Box(
                Modifier
                    .fillMaxWidth(0.85f)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PalavraoTheme.colors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("ESPAÇO RESERVADO PARA\nA LOGO DO JOGO")
            }

            Spacer(Modifier.height(32.dp))

            LargeMenuButton("MODO AVALANCHE") {
                nav.navigate(PalavraoScreen.AvalancheScreen)
            }
            LargeMenuButton("PALAVRA DO DIA") {
                nav.navigate(PalavraoScreen.DailyScreen)
            }
            LargeMenuButton("DESAFIO") {
                nav.navigate(PalavraoScreen.FriendChallengeScreen)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SmallMenuButton("ESTATÍSTICA") { nav.navigate(PalavraoScreen.StatsScreen) }
                SmallMenuButton("CONFIGURAÇÃO") { nav.navigate(PalavraoScreen.SettingsScreen) }
                SmallMenuButton("SOBRE NÓS") { nav.navigate(PalavraoScreen.AboutScreen) }
            }
        }
    }
}

@Composable
private fun LargeMenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PalavraoTheme.colors.surfaceVariant,
            contentColor = PalavraoTheme.colors.onSurface
        )
    ) { Text(text) }
}

@Composable
private fun SmallMenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .widthIn(min = 110.dp)
            .height(40.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PalavraoTheme.colors.surfaceVariant,
            contentColor = PalavraoTheme.colors.onSurface
        )
    ) { Text(text) }
}