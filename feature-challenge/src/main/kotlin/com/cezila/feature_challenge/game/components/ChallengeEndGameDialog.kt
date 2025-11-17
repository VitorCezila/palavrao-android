package com.cezila.feature_challenge.game.components

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
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.component.RoundedButton
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.feature_challenge.game.ChallengeGameUiState

@Composable
fun ChallengeEndGameDialog(
    modifier: Modifier = Modifier,
    uiState: ChallengeGameUiState,
    onShare: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = if (uiState.won) "PARABÉNS \uD83C\uDF89\uD83C\uDF89" else "QUE PENA \uD83D\uDE14"
    AlertDialog(
        modifier = modifier,
        containerColor = PalavraoTheme.background.color,
        textContentColor = PalavraoTheme.colors.primary,
        titleContentColor = PalavraoTheme.colors.primary,
        onDismissRequest = { onDismiss() },
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
                    text = "A palavra era:",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${uiState.round?.target?.value?.uppercase()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
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