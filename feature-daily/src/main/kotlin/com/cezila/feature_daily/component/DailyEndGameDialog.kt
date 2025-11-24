package com.cezila.feature_daily.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
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
import com.cezila.feature_daily.DailyUiState

@Composable
fun DailyEndGameDialog(
    modifier: Modifier = Modifier,
    uiState: DailyUiState,
    onShare: () -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    val title = if (uiState.won) {
        stringResource(R.string.game_won_title)
    } else {
        stringResource(R.string.game_lost_title)
    }

    val streakText = if (uiState.won) {
        stringResource(R.string.streak_current_format, uiState.currentStreak)
    } else {
        stringResource(R.string.streak_broken)
    }
    AlertDialog(
        modifier = modifier,
        containerColor = PalavraoTheme.background.color,
        textContentColor = PalavraoTheme.colors.primary,
        titleContentColor = PalavraoTheme.colors.primary,
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.the_word_was),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${uiState.round?.target?.value?.uppercase()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = streakText,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
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
        DailyEndGameDialog(
            uiState = DailyUiState().copy(
                loading = false,
                won = false,
                currentInput = listOf('T', 'E', 'S', 'T', 'E'),
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
            onShare = {}
        )
    }
}

@Preview
@Composable
private fun EndGameDialogPreviewWon() {
    PalavraoTheme {
        DailyEndGameDialog(
            uiState = DailyUiState().copy(
                loading = false,
                won = true,
                currentInput = listOf('T', 'E', 'S', 'T', 'E'),
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
            onShare = {}
        )
    }
}