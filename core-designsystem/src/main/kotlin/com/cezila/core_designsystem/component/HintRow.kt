package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme

@Composable
fun HintRow(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = color,
            shape = MaterialTheme.shapes.small
        ) { Spacer(Modifier.size(20.dp)) }
        Text(text = label, color = PalavraoTheme.colors.primary)
    }
}

@Preview
@Composable
private fun HintRowPreview() {
    PalavraoTheme {
        HintRow("Letra não está na palavra", Color.Red)
    }
}