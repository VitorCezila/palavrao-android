package com.cezila.feature_avalanche

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.component.HintRow
import com.cezila.core_designsystem.component.PalavraoBoard
import com.cezila.core_designsystem.component.PalavraoKeyboard
import com.cezila.core_designsystem.component.RoundedButton
import com.cezila.core_designsystem.currentSnackbarHostState
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.core_designsystem.utils.GameShareManager
import com.cezila.core_navigation.currentComposeNavigator
import com.cezila.domain.error.SubmitGuessError
import com.cezila.feature_avalanche.component.EndGameDialog

@Composable
fun AvalancheScreen(
    vm: AvalancheViewModel = hiltViewModel<AvalancheViewModel>()
) {
    val snackbarHostState = currentSnackbarHostState
    val composeNavigator = currentComposeNavigator
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val shareManager = remember(context) { GameShareManager(context) }

    LaunchedEffect(key1 = Unit) {
        vm.start()
        vm.events.collect { event ->
            when (event) {
                is AvalancheEvent.ShowGuessError -> {
                    val message = when (event.message) {
                        SubmitGuessError.ROUND_FINISHED -> context.getString(R.string.round_already_finished)
                        SubmitGuessError.INVALID_LENGTH -> context.getString(R.string.invalid_length)
                        SubmitGuessError.INVALID_CHARS -> context.getString(R.string.invalid_chars)
                        SubmitGuessError.INVALID_WORD -> context.getString(R.string.invalid_word)
                    }
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = context.getString(R.string.ok)
                    )
                }
            }
        }
    }

    AvalancheScreenContent(
        uiState = state,
        onTryAgain = vm::start,
        onPlayAgain = vm::playAgain,
        onShare = {
            shareManager.shareAvalanche(
                word = vm.state.value.round?.target?.value,
                streak = vm.state.value.currentStreak
            )
        },
        onBackPressed = composeNavigator::navigateUp,
        onToggleHelp = vm::toggleHelp,
        onKeyPressed = vm::onKey,
        onEnterPressed = vm::onEnter,
        onBackspacePressed = vm::onBackspace,
        onTileSelected = vm::onTileSelected,
        onDismissEndDialog = vm::hideEndDialog,
        onShowEndDialog = vm::showEndDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvalancheScreenContent(
    uiState: AvalancheUiState,
    onTryAgain: () -> Unit,
    onPlayAgain: () -> Unit,
    onShare: () -> Unit,
    onBackPressed: () -> Unit,
    onToggleHelp: (Boolean) -> Unit,
    onKeyPressed: (Char) -> Unit,
    onEnterPressed: () -> Unit,
    onBackspacePressed: () -> Unit,
    onTileSelected: (Int) -> Unit,
    onDismissEndDialog: () -> Unit,
    onShowEndDialog: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.avalanche_title)) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrow_back_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleHelp(true) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = stringResource(R.string.help)
                        )
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
                                maxAttempts = 6,
                                selectedCol = uiState.currentCursor,
                                onTileClick = onTileSelected
                            )
                        }
                        if (uiState.finished) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                RoundedButton(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(45.dp),
                                    text = stringResource(R.string.next),
                                    onClick = { onPlayAgain() }
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                IconButton(
                                    onClick = { onShowEndDialog() },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = PalavraoTheme.colors.primary,
                                        containerColor = PalavraoTheme.colors.surface
                                    ),
                                    modifier = Modifier.size(45.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BarChart,
                                        contentDescription = "Statistics"
                                    )
                                }
                            }
                        } else {
                            RoundedButton(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(45.dp),
                                text = stringResource(R.string.send),
                                onClick = { onEnterPressed() }
                            )
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
    if (uiState.finished && uiState.isEndDialogVisible) {
        EndGameDialog(
            uiState = uiState,
            onPlayAgain = onPlayAgain,
            onShare = onShare,
            onDismissRequest = onDismissEndDialog
        )
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
                    Text(text = stringResource(R.string.ok), color = PalavraoTheme.colors.primary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.avalanche_description),
                        softWrap = true,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    HintRow(
                        stringResource(R.string.absent_description),
                        PalavraoTheme.colors.absent
                    )
                    HintRow(
                        stringResource(R.string.present_description),
                        PalavraoTheme.colors.present
                    )
                    HintRow(
                        stringResource(R.string.correct_description),
                        PalavraoTheme.colors.correct
                    )
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
                currentInput = listOf('T', 'E', 'S', 'T', 'E')
            ),
            onBackPressed = {},
            onToggleHelp = {},
            onKeyPressed = {},
            onEnterPressed = {},
            onBackspacePressed = {},
            onTryAgain = { },
            onPlayAgain = {},
            onShare = {},
            onTileSelected = {},
            onDismissEndDialog = {},
            onShowEndDialog = {}
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
                currentInput = listOf('T', 'E', 'S', 'T', 'E'),
                startRoundFailed = true
            ),
            onBackPressed = {},
            onToggleHelp = {},
            onKeyPressed = {},
            onEnterPressed = {},
            onBackspacePressed = {},
            onTryAgain = {},
            onPlayAgain = {},
            onShare = {},
            onTileSelected = {},
            onDismissEndDialog = {},
            onShowEndDialog = {}
        )
    }
}