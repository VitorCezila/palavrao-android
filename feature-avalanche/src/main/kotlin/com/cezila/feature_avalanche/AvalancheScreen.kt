package com.cezila.feature_avalanche

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.component.PalavraoBoard
import com.cezila.core_designsystem.component.PalavraoKeyboard
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.currentComposeNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvalancheScreen(
    vm: AvalancheViewModel = hiltViewModel<AvalancheViewModel>()
) {
    val composeNavigator = currentComposeNavigator

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.start() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AVALANCHE") },
                navigationIcon = {
                    IconButton(onClick = { composeNavigator.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { vm.toggleHelp(true) }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "ajuda")
                    }
                }
            )
        },
        bottomBar = {
            PalavraoKeyboard(
                enabled = !state.finished && state.round != null,
                onKey = vm::onKey,
                onEnter = vm::onEnter,
                onBackspace = vm::onBackspace
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                state.loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.error != null -> Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.error!!)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = vm::start) { Text("Tentar novamente") }
                }

                else -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))
                    PalavraoBoard(
                        rows = state.rows,
                        currentInput = state.currentInput,
                        wordLength = 5,
                        maxAttempts = 6
                    )
                    Spacer(Modifier.height(16.dp))
                    val msg = when {
                        state.finished && state.won -> "Você venceu!"
                        state.finished -> "Fim de jogo."
                        else -> ""
                    }
                    if (msg.isNotEmpty()) Text(msg, color = PalavraoTheme.colors.onSurface)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }

    if (state.showHelp) {
        AlertDialog(
            onDismissRequest = { vm.toggleHelp(false) },
            confirmButton = {
                TextButton(onClick = { vm.toggleHelp(false) }) { Text("Entendi") }
            },
            title = { Text("Como jogar") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("No modo avalanche você tenta acertar o máximo de palavras seguidas.")
                    LegendRow("Letra não está na palavra", PalavraoTheme.colors.absent)
                    LegendRow("Existe (posição errada)", PalavraoTheme.colors.present)
                    LegendRow("Correta", PalavraoTheme.colors.correct)
                }
            }
        )
    }
}

@Composable
private fun LegendRow(label: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = color,
            shape = MaterialTheme.shapes.small
        ) { Spacer(Modifier.size(20.dp)) }
        Text(label)
    }
}