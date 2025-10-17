package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
        filledRows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { (ch, state) ->
                    Tile(ch, state)
                }
            }
        }
        if (pendingCount > 0) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in 0 until wordLength) {
                    val ch = currentInput.getOrNull(i) ?: ' '
                    Tile(ch, null)
                }
            }
            repeat(pendingCount - 1) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(wordLength) { Tile(' ', null) }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PalavraoBoardPreview() {
    PalavraoTheme {
        PalavraoBoard(
            rows = listOf(
                listOf(
                    'T' to LetterState.CORRECT,
                    'E' to LetterState.CORRECT,
                    'S' to LetterState.CORRECT,
                    'T' to LetterState.CORRECT,
                    'A' to LetterState.CORRECT
                ),
                listOf(
                    'T' to LetterState.PRESENT,
                    'E' to LetterState.PRESENT,
                    'S' to LetterState.PRESENT,
                    'T' to LetterState.PRESENT,
                    'A' to LetterState.PRESENT
                ),
                listOf(
                    'T' to LetterState.ABSENT,
                    'E' to LetterState.ABSENT,
                    'S' to LetterState.ABSENT,
                    'T' to LetterState.ABSENT,
                    'A' to LetterState.ABSENT
                ),
            ),
            currentInput = "",
            wordLength = 5,
            maxAttempts = 6
        )
    }
}