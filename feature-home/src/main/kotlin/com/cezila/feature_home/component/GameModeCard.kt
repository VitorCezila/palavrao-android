package com.cezila.feature_home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardColors(
            containerColor = PalavraoTheme.colors.surface,
            contentColor = PalavraoTheme.colors.primary,
            disabledContainerColor = PalavraoTheme.colors.surface,
            disabledContentColor = PalavraoTheme.colors.primary
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = PalavraoTheme.colors.primary
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = title.toUpperCase(Locale.current),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(locale = "pt", widthDp = 320)
@Composable
private fun GameModeCardPreview() {
    PalavraoTheme {
        GameModeCard(
            icon = Icons.Default.LocalFireDepartment,
            title = "Modo Avalanche",
            description = "Acerte o máximo de palavras que puder.",
            onClick = {}
        )
    }
}