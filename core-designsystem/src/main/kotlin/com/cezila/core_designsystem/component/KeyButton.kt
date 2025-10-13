package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.domain.model.LetterState

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    state: LetterState? = null
) {
    val bg = when (state) {
        LetterState.CORRECT -> PalavraoTheme.colors.correct
        LetterState.PRESENT -> PalavraoTheme.colors.present
        LetterState.ABSENT -> PalavraoTheme.colors.absent
        null -> PalavraoTheme.colors.surfaceVariant
    }
    val fg = PalavraoTheme.colors.primary

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(44.dp),
        shape = RoundedCornerShape(22.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonColors(
            containerColor = bg,
            contentColor = fg,
            disabledContainerColor = bg,
            disabledContentColor = fg,
        )
    ) {
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview
@Composable
private fun KeyButtonPreview() {
    PalavraoTheme {
        KeyButton(label = "ENTER", enabled = true, onClick = {})
    }
}