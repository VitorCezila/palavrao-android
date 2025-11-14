@file:OptIn(ExperimentalMaterial3Api::class)

package com.cezila.feature_stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.feature_stats.components.AvalancheStatsPage
import com.cezila.feature_stats.components.DailyStatsPage
import kotlinx.coroutines.launch

private val TABS = listOf("AVALANCHE", "DIÁRIO", "AMIGOS")

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel<StatsViewModel>()
) {
    val navigator = currentComposeNavigator
    val uiState by viewModel.state.collectAsState()
    val pagerState = rememberPagerState { TABS.size }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ESTATÍSTICAS") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarColors(
                    containerColor = PalavraoTheme.background.color,
                    scrolledContainerColor = PalavraoTheme.background.color,
                    navigationIconContentColor = PalavraoTheme.colors.primary,
                    titleContentColor = PalavraoTheme.colors.primary,
                    actionIconContentColor = PalavraoTheme.colors.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PalavraoTheme.background.color)
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = PalavraoTheme.colors.primary
                        )
                    }
                }
            ) {
                TABS.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.background(PalavraoTheme.background.color),
                        selectedContentColor = PalavraoTheme.colors.primary,
                        unselectedContentColor = PalavraoTheme.colors.primary,
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> AvalancheStatsPage(stats = uiState.avalancheStats)
                    1 -> DailyStatsPage(stats = uiState.dailyStats)
                    2 -> PlaceholderStatsPage(title = "Histórico de Desafios de Amigos")
                }
            }
        }
    }
}

@Composable
private fun PlaceholderStatsPage(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title ainda não implementado.\n\n(Necessário adicionar persistência de histórico no módulo 'data' e 'domain'.)",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}