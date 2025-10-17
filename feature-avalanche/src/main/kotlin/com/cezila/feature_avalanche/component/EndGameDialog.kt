package com.cezila.feature_avalanche.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.component.RoundedButton
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.domain.model.GuessResult
import com.cezila.domain.model.LetterFeedback
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.model.Word
import com.cezila.feature_avalanche.AvalancheUiState

@Composable
fun EndGameDialog(
    modifier: Modifier = Modifier,
    uiState: AvalancheUiState,
    onPlayAgain: () -> Unit,
    onShare: () -> Unit
) {
    val title = if (uiState.won) "PARABÉNS!" else "NÃO FOI DESTA VEZ..."
    val streakText = if (uiState.won) {
        "Sequência Atual: ${uiState.currentStreak} 🔥"
    } else {
        "Sequência Quebrada 😢"
    }
    AlertDialog(
        modifier = modifier,
        containerColor = PalavraoTheme.background.color,
        textContentColor = PalavraoTheme.colors.primary,
        titleContentColor = PalavraoTheme.colors.primary,
        onDismissRequest = onPlayAgain,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "A palavra era: ${uiState.round?.target?.value?.uppercase()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = streakText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RoundedButton(
                    modifier = Modifier.width(140.dp),
                    text = stringResource(R.string.next),
                    onClick = { onPlayAgain() },
                    fontSize = 12
                )
                Spacer(modifier = Modifier.height(16.dp))
                RoundedButton(
                    modifier = Modifier.width(140.dp),
                    text = stringResource(R.string.share),
                    onClick = { onShare() },
                    fontSize = 12
                )

            }
        }
    )
}

@Preview
@Composable
private fun EndGameDialogPreviewLoose() {
    PalavraoTheme {
        EndGameDialog(
            uiState = AvalancheUiState().copy(
                loading = false,
                won = false,
                currentInput = "TESTA",
                round = Round(
                    target = Word.w("SALAO"),
                    attempts = emptyList(),
                    status = RoundStatus.LOST,
                    lastResult = GuessResult(
                        feedback = listOf(
                            LetterFeedback(
                                letter = 'A',
                                state = LetterState.CORRECT
                            )
                        ),
                        isWin = false
                    )
                ),
                currentStreak = 0
            ),
            onPlayAgain = {},
            onShare = {}
        )
    }
}

@Preview
@Composable
private fun EndGameDialogPreviewWon() {
    PalavraoTheme {
        EndGameDialog(
            uiState = AvalancheUiState().copy(
                loading = false,
                won = true,
                currentInput = "TESTA",
                round = Round(
                    target = Word.w("SALAO"),
                    attempts = emptyList(),
                    status = RoundStatus.WON,
                    lastResult = GuessResult(
                        feedback = listOf(
                            LetterFeedback(
                                letter = 'A',
                                state = LetterState.CORRECT
                            )
                        ),
                        isWin = true
                    )
                ),
                currentStreak = 5
            ),
            onPlayAgain = {},
            onShare = {}
        )
    }
}