package com.cezila.feature_avalanche

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.component.HintRow
import com.cezila.core_designsystem.component.PalavraoBoard
import com.cezila.core_designsystem.component.PalavraoKeyboard
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.error.SubmitGuessError
import com.cezila.domain.model.Round

@Composable
fun AvalancheScreen(
    vm: AvalancheViewModel = hiltViewModel<AvalancheViewModel>()
) {
    val composeNavigator = currentComposeNavigator
    val state by vm.state.collectAsState()

    AvalancheScreenContent(
        uiState = state,
        startAvalancheRound = vm::start,
        onBackPressed = composeNavigator::navigateUp,
        onToggleHelp = vm::toggleHelp,
        onKeyPressed = vm::onKey,
        onEnterPressed = vm::onEnter,
        onBackspacePressed = vm::onBackspace
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvalancheScreenContent(
    uiState: AvalancheUiState,
    startAvalancheRound: () -> Unit,
    onBackPressed: () -> Unit,
    onToggleHelp: (Boolean) -> Unit,
    onKeyPressed: (Char) -> Unit,
    onEnterPressed: () -> Unit,
    onBackspacePressed: () -> Unit,
) {
    LaunchedEffect(Unit) { startAvalancheRound() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AVALANCHE") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleHelp(true) }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "ajuda")
                    }
                }
            )
        },
        bottomBar = {

        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.submitError != null -> Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val message = when (uiState.submitError) {
                        SubmitGuessError.ROUND_FINISHED -> "Rodada finalizada"
                        SubmitGuessError.INVALID_LENGTH -> "Tamanho da palavra inválido"
                        SubmitGuessError.INVALID_CHARS -> "Caracteres inválidos"
                        SubmitGuessError.INVALID_WORD -> "Palavra inválida"
                    }
                    Text(message)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { startAvalancheRound() }) { Text("Tentar novamente") }
                }

                uiState.startRoundError != null -> Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val message = when (uiState.startRoundError) {
                        StartAvalancheRoundError.COMMUNICATION_ERROR -> "Erro de comunicação"
                    }
                    Text(message)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { startAvalancheRound() }) { Text("Tentar novamente") }
                }

                else -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PalavraoBoard(
                        rows = uiState.rows,
                        currentInput = uiState.currentInput,
                        wordLength = 5,
                        maxAttempts = 6
                    )
                    Spacer(Modifier.height(16.dp))
                    val msg = when {
                        uiState.finished && uiState.won -> "Você venceu!"
                        uiState.finished -> "Fim de jogo."
                        else -> ""
                    }
                    if (msg.isNotEmpty()) Text(msg, color = PalavraoTheme.colors.onSurface)
                    Spacer(Modifier.height(8.dp))
                    PalavraoKeyboard(
                        enabled = !uiState.finished && uiState.round != null,
                        onKey = { onKeyPressed(it) },
                        onEnter = { onEnterPressed() },
                        onBackspace = onBackspacePressed,
                        keyStates = uiState.keyStates
                    )
                }
            }
        }
    }

    if (uiState.showHelp) {
        AlertDialog(
            onDismissRequest = { onToggleHelp(false) },
            confirmButton = {
                TextButton(onClick = { onToggleHelp(false) }) { Text("Entendi") }
            },
            title = { Text("Como jogar") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("No modo avalanche você tenta acertar o máximo de palavras seguidas.")
                    HintRow("Letra não está na palavra", PalavraoTheme.colors.absent)
                    HintRow("Existe (posição errada)", PalavraoTheme.colors.present)
                    HintRow("Correta", PalavraoTheme.colors.correct)
                }
            }
        )
    }
}

@Preview
@Composable
private fun AvalancheScreenPreview() {
    PalavraoTheme {
        AvalancheScreenContent(
            uiState = AvalancheUiState().copy(
                loading = false,
                submitError = null,
                startRoundError = null,
                currentInput = "TESTA"
            ),
            startAvalancheRound = {},
            onBackPressed = {},
            onToggleHelp = {},
            onKeyPressed = {},
            onEnterPressed = {},
            onBackspacePressed = {}
        )
    }
}