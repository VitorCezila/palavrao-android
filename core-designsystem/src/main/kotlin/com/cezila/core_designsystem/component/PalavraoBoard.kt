package com.cezila.core_designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.domain.model.LetterState


@Composable
fun PalavraoBoard(
    rows: List<List<Pair<Char, LetterState>>>,
    currentInput: String,
    wordLength: Int = 5,
    maxAttempts: Int = 6
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        val filledRows = rows.map { it }
        val pendingCount = (maxAttempts - filledRows.size).coerceAtLeast(0)

        // linhas já avaliadas
        filledRows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { (ch, state) ->
                    Tile(ch, state)
                }
            }
        }
        // linha digitando agora
        if (pendingCount > 0) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in 0 until wordLength) {
                    val ch = currentInput.getOrNull(i) ?: ' '
                    Tile(ch, null)
                }
            }
            // linhas vazias restantes
            repeat(pendingCount - 1) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(wordLength) { Tile(' ', null) }
                }
            }
        }
    }
}

@Composable
fun Tile(
    ch: Char,
    state: LetterState?
) {
    val color = when (state) {
        LetterState.CORRECT -> PalavraoTheme.colors.correct
        LetterState.PRESENT -> PalavraoTheme.colors.present
        LetterState.ABSENT -> PalavraoTheme.colors.absent
        null -> PalavraoTheme.colors.surfaceVariant
    }
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            if (ch == ' ') "" else ch.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = PalavraoTheme.colors.onSurface
        )
    }
}