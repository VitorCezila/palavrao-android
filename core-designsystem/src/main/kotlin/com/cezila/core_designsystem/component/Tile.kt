package com.cezila.core_designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.domain.model.LetterState

@Composable
fun Tile(
    ch: Char,
    state: LetterState?,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val color = when (state) {
        LetterState.CORRECT -> PalavraoTheme.colors.correct
        LetterState.PRESENT -> PalavraoTheme.colors.present
        LetterState.ABSENT -> PalavraoTheme.colors.absent
        null -> PalavraoTheme.background.color
    }

    val tileBorder = if (isSelected) {
        BorderStroke(2.dp, PalavraoTheme.colors.focus)
    } else {
        BorderStroke(1.dp, PalavraoTheme.colors.primary)
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable(
                enabled = state == null,
                onClick = onClick
            )
            .border(tileBorder, RoundedCornerShape(6.dp))
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            if (ch == ' ') "" else ch.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = PalavraoTheme.colors.primary
        )
    }
}

@Preview
@Composable
private fun TilePreview() {
    PalavraoTheme {
        Tile(
            ch = 'A',
            state = LetterState.ABSENT,
            isSelected = false
        )
    }
}