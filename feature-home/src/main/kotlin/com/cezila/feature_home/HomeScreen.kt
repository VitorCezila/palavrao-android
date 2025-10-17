package com.cezila.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.feature_home.component.GameModeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val nav = currentComposeNavigator
    HomeContent(
        modifier = Modifier,
        onAvalancheClick = { nav.navigate(PalavraoScreen.AvalancheScreen) },
        onDailyClick = { nav.navigate(PalavraoScreen.DailyScreen) },
        onFriendChallengeClick = { nav.navigate(PalavraoScreen.FriendChallengeScreen) },
        onStatsClick = { nav.navigate(PalavraoScreen.StatsScreen) },
        onSettingsClick = { nav.navigate(PalavraoScreen.SettingsScreen) },
        onToggleHelp = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onAvalancheClick: () -> Unit,
    onDailyClick: () -> Unit,
    onFriendChallengeClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onToggleHelp: (Boolean) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {

                },
                actions = {
                    IconButton(onClick = { onToggleHelp(true) }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "ajuda")
                    }
                },
                colors = TopAppBarColors(
                    containerColor = PalavraoTheme.background.color,
                    scrolledContainerColor = PalavraoTheme.background.color,
                    navigationIconContentColor = PalavraoTheme.colors.primary,
                    titleContentColor = PalavraoTheme.colors.primary,
                    actionIconContentColor = PalavraoTheme.colors.primary
                ),
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PalavraoTheme.background.color)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "PALAVRAO",
                fontWeight = FontWeight.Bold,
                color = PalavraoTheme.colors.primary,
                fontSize = 36.sp,
                letterSpacing = TextUnit(10f, TextUnitType.Sp)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            GameModeCard(
                icon = Icons.Default.LocalFireDepartment,
                title = "Modo Avalanche",
                description = "Acerte o máximo de palavras que puder.",
                onClick = { onAvalancheClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GameModeCard(
                icon = Icons.Default.CalendarMonth,
                title = "Palavra do Dia",
                description = "Adivinhe a palavra de hoje.",
                onClick = { onDailyClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GameModeCard(
                icon = Icons.Default.Groups,
                title = "Desafiar Amigos",
                description = "Crie ou jogue desafios.",
                onClick = { onFriendChallengeClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Button(
                    modifier = Modifier
                        .width(130.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonColors(
                        containerColor = PalavraoTheme.colors.surface,
                        contentColor = PalavraoTheme.colors.primary,
                        disabledContainerColor = PalavraoTheme.colors.surface,
                        disabledContentColor = PalavraoTheme.colors.primary
                    ),
                    onClick = { onStatsClick() }
                ) {
                    Text(text = "ESTATISTICAS", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(32.dp))

                Button(
                    modifier = Modifier
                        .width(130.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonColors(
                        containerColor = PalavraoTheme.colors.surface,
                        contentColor = PalavraoTheme.colors.primary,
                        disabledContainerColor = PalavraoTheme.colors.surface,
                        disabledContentColor = PalavraoTheme.colors.primary
                    ),
                    onClick = { onSettingsClick() }
                ) {
                    Text(text = "OPCOES", fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    PalavraoTheme {
        HomeContent(
            onAvalancheClick = {},
            onDailyClick = {},
            onFriendChallengeClick = {},
            onStatsClick = {},
            onSettingsClick = {},
            onToggleHelp = {}
        )
    }
}