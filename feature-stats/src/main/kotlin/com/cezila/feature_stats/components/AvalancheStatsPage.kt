package com.cezila.feature_stats.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.domain.model.avalanche.AvalancheStats

@Composable
fun AvalancheStatsPage(stats: AvalancheStats) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                label = stringResource(R.string.games_played),
                value = stats.gamesPlayed.toString(),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                label = stringResource(R.string.current_streak),
                value = stats.currentStreak.toString(),
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                label = stringResource(R.string.best_streak),
                value = stats.maxStreak.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatsCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .background(PalavraoTheme.colors.surface),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardColors(
            containerColor = PalavraoTheme.colors.surface,
            contentColor = PalavraoTheme.colors.primary,
            disabledContainerColor = PalavraoTheme.colors.surface,
            disabledContentColor = PalavraoTheme.colors.primary
        ),
        border = BorderStroke(1.dp, PalavraoTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Preview(locale = "pt", widthDp = 320)
@Composable
private fun AvalancheStatsPagePreview() {
    PalavraoTheme {
        AvalancheStatsPage(
            stats = AvalancheStats(
                gamesPlayed = 10,
                currentStreak = 5,
                maxStreak = 10
            )
        )
    }
}

@Preview(locale = "pt", widthDp = 320)
@Composable
private fun StatsCardPreview() {
    PalavraoTheme {
        StatsCard(
            label = "Jogos Jogados",
            value = "10"
        )
    }
}