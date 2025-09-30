package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PalavraoKeyboard(
    onKey: (Char) -> Unit,
    onEnter: () -> Unit,
    onBackspace: () -> Unit,
    enabled: Boolean
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
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (idx == 2) {
                    KeyButton("ENTER", enabled = enabled, onClick = onEnter)
                }
                line.forEach { c ->
                    KeyButton(c.toString(), enabled = enabled) { onKey(c) }
                }
                if (idx == 2) {
                    KeyButton("⌫", enabled = enabled, onClick = onBackspace)
                }
            }
        }
    }
}

@Composable
fun KeyButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.height(44.dp)
    ) { Text(label) }
}