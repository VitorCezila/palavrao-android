package com.cezila.feature_avalanche

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.component.HintRow
import com.cezila.core_designsystem.component.PalavraoBoard
import com.cezila.core_designsystem.component.PalavraoKeyboard
import com.cezila.core_designsystem.currentSnackbarHostState
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.domain.error.SubmitGuessError

@Composable
fun AvalancheScreen(
    vm: AvalancheViewModel = hiltViewModel<AvalancheViewModel>()
) {
    val snackbarHostState = currentSnackbarHostState
    val composeNavigator = currentComposeNavigator
    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.start()
        vm.events.collect { event ->
            when (event) {
                is AvalancheEvent.ShowError -> {
                    val message = when (event.message) {
                        SubmitGuessError.ROUND_FINISHED -> "A rodada já terminou"
                        SubmitGuessError.INVALID_LENGTH -> "A palavra deve ter 5 letras"
                        SubmitGuessError.INVALID_CHARS -> "Caracteres inválidos"
                        SubmitGuessError.INVALID_WORD -> "Essa palavra não existe"
                    }
                    snackbarHostState.showSnackbar(message = message, actionLabel = "OK")
                }
            }
        }
    }

    AvalancheScreenContent(
        uiState = state,
        onTryAgain = vm::start,
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
    onTryAgain: () -> Unit,
    onBackPressed: () -> Unit,
    onToggleHelp: (Boolean) -> Unit,
    onKeyPressed: (Char) -> Unit,
    onEnterPressed: () -> Unit,
    onBackspacePressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PalavraoTheme.background.color)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.loading -> CircularProgressIndicator()
                uiState.startRoundFailed -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Falha ao iniciar a rodada.")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onTryAgain) {
                            Text("Tentar Novamente")
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.height(1.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        }
                        Button(
                            modifier = Modifier
                                .width(120.dp)
                                .height(45.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonColors(
                                containerColor = PalavraoTheme.colors.surface,
                                contentColor = PalavraoTheme.colors.primary,
                                disabledContainerColor = PalavraoTheme.colors.surface,
                                disabledContentColor = PalavraoTheme.colors.primary
                            ),
                            onClick = { onEnterPressed() }
                        ) {
                            Text("ENVIAR")
                        }
                        PalavraoKeyboard(
                            enabled = !uiState.finished && uiState.round != null,
                            onKey = onKeyPressed,
                            onBackspace = onBackspacePressed,
                            keyStates = uiState.keyStates
                        )
                    }
                }
            }
        }
    }
    if (uiState.showHelp) {
        AlertDialog(
            containerColor = PalavraoTheme.background.color,
            textContentColor = PalavraoTheme.colors.primary,
            onDismissRequest = { onToggleHelp(false) },
            confirmButton = {
                TextButton(
                    onClick = { onToggleHelp(false) }
                ) {
                    Text(text = "Entendi", color = PalavraoTheme.colors.primary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "O modo avalanche consiste em tentar fazer a maior quantidade de acertos possíveis para garantir uma sequência maior.",
                        softWrap = true,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
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
                currentInput = "TESTA"
            ),
            onBackPressed = {},
            onToggleHelp = {},
            onKeyPressed = {},
            onEnterPressed = {},
            onBackspacePressed = {},
            onTryAgain = { }
        )
    }
}

@Preview
@Composable
private fun AvalancheScreenPreviewStartRoundFailed() {
    PalavraoTheme {
        AvalancheScreenContent(
            uiState = AvalancheUiState().copy(
                loading = false,
                currentInput = "TESTA",
                startRoundFailed = true
            ),
            onBackPressed = {},
            onToggleHelp = {},
            onKeyPressed = {},
            onEnterPressed = {},
            onBackspacePressed = {},
            onTryAgain = {}
        )
    }
}