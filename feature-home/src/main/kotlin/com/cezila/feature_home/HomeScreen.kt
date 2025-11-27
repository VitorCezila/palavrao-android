package com.cezila.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.component.RoundedButton
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.feature_home.component.GameModeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val nav = currentComposeNavigator

    val showHelp = remember { mutableStateOf(false) }

    HomeContent(
        modifier = modifier,
        onAvalancheClick = { nav.navigate(PalavraoScreen.AvalancheScreen) },
        onDailyClick = { nav.navigate(PalavraoScreen.DailyScreen) },
        onFriendChallengeClick = { nav.navigate(PalavraoScreen.FriendChallengeHomeScreen) },
        onStatsClick = { nav.navigate(PalavraoScreen.StatsScreen) },
        onSettingsClick = { nav.navigate(PalavraoScreen.SettingsScreen) },
        onToggleHelp = {
            showHelp.value = it
        },
        showHelp.value
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
    showHelp: Boolean
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
                text = stringResource(R.string.app_name).uppercase(),
                fontWeight = FontWeight.Bold,
                color = PalavraoTheme.colors.primary,
                fontSize = 36.sp,
                letterSpacing = TextUnit(10f, TextUnitType.Sp)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Button(onClick = { throw RuntimeException("Teste teste lytics palavreando!") }) {
                Text("Forçar Crash")
            }

            GameModeCard(
                icon = Icons.Default.LocalFireDepartment,
                title = stringResource(R.string.avalanche_mode),
                description = stringResource(R.string.avalanche_short_description),
                onClick = { onAvalancheClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GameModeCard(
                icon = Icons.Default.CalendarMonth,
                title = stringResource(R.string.daily_mode),
                description = stringResource(R.string.daily_short_description),
                onClick = { onDailyClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GameModeCard(
                icon = Icons.Default.Groups,
                title = stringResource(R.string.challenge_mode),
                description = stringResource(R.string.challenge_short_description),
                onClick = { onFriendChallengeClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                RoundedButton(
                    modifier = Modifier
                        .width(130.dp)
                        .height(45.dp),
                    text = stringResource(R.string.statistics),
                    fontSize = 12,
                    onClick = { onStatsClick() }
                )

                Spacer(modifier = Modifier.width(32.dp))

                RoundedButton(
                    modifier = Modifier
                        .width(130.dp)
                        .height(45.dp),
                    text = stringResource(R.string.settings),
                    fontSize = 12,
                    onClick = { onSettingsClick() }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (showHelp) {
                AlertDialog(
                    containerColor = PalavraoTheme.background.color,
                    textContentColor = PalavraoTheme.colors.primary,
                    onDismissRequest = { onToggleHelp(false) },
                    confirmButton = {
                        TextButton(
                            onClick = { onToggleHelp(false) }
                        ) {
                            Text(text = stringResource(R.string.ok), color = PalavraoTheme.colors.primary)
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.app_description),
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.avalanche_description),
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.daily_description),
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.challenge_description),
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }
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
            onToggleHelp = {},
            showHelp = false
        )
    }
}