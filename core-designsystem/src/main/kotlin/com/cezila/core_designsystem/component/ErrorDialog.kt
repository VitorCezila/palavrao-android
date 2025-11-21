package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.cezila.core_designsystem.R
import com.cezila.core_designsystem.theme.PalavraoTheme

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit,
    onDismissRequest: () -> Unit,
    description: String
) {
    AlertDialog(
        modifier = modifier,
        containerColor = PalavraoTheme.background.color,
        textContentColor = PalavraoTheme.colors.primary,
        titleContentColor = PalavraoTheme.colors.primary,
        onDismissRequest = { onDismissRequest() },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "Error",
                    tint = PalavraoTheme.colors.error,
                    modifier = Modifier
                        .size(48.dp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Oops!",
                    fontWeight = FontWeight.Bold,
                    color = PalavraoTheme.colors.primary,
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = description,
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
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.try_again),
                    onClick = { onTryAgain() },
                    fontSize = 12
                )
            }
        }
    )

}

@Preview
@Composable
private fun ErrorDialogPreview() {
    PalavraoTheme {
        ErrorDialog(
            description = "Não foi possivel buscar a palavra, Verifique sua conexão e tente novamente.",
            onTryAgain = {},
            onDismissRequest = {}
        )
    }
}