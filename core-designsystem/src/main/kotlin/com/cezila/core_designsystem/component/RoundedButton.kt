package com.cezila.core_designsystem.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cezila.core_designsystem.theme.PalavraoTheme

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    fontSize: Int = 14
) {
    Button(
        modifier = modifier
            .width(120.dp)
            .height(45.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonColors(
            containerColor = PalavraoTheme.colors.surface,
            contentColor = PalavraoTheme.colors.primary,
            disabledContainerColor = PalavraoTheme.colors.surface,
            disabledContentColor = PalavraoTheme.colors.primary
        ),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp
        )
    }
}