package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme
import com.cezila.domain.model.LetterState

@Composable
fun PalavraoKeyboard(
    onKey: (Char) -> Unit,
    onEnter: () -> Unit,
    onBackspace: () -> Unit,
    enabled: Boolean,
    keyStates: Map<Char, LetterState> = emptyMap()
) {
    val rows = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rows.forEachIndexed { idx, line ->
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (idx == 2) {
                    KeyButton(
                        modifier = Modifier.weight(1.5f, fill = true),
                        "ENTER",
                        enabled = enabled,
                        onClick = onEnter,
                    )
                }
                line.forEach { c ->
                    KeyButton(
                        modifier = Modifier.weight(1f, fill = true),
                        c.toString(),
                        enabled = enabled,
                        onClick = { onKey(c) },
                        state = keyStates[c],
                    )
                }
                if (idx == 2) {
                    KeyButton(
                        modifier = Modifier.weight(1.5f, fill = true),
                        "⌫",
                        enabled = enabled,
                        onClick = onBackspace,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PalavraoKeyboardPreview() {
    PalavraoTheme {
        PalavraoKeyboard(
            onKey = {},
            onEnter = {},
            onBackspace = {},
            enabled = true
        )
    }
}