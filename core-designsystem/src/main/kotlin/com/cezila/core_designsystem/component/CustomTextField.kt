package com.cezila.core_designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.theme.PalavraoTheme

@Composable
fun CustomWordTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    error: String = "",
    isSingleLine: Boolean = true,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(text = hint)
        },
        isError = error.isNotEmpty(),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = PalavraoTheme.colors.primary,
            unfocusedTextColor = PalavraoTheme.colors.primary,
            focusedPlaceholderColor = PalavraoTheme.colors.primary,
            unfocusedPlaceholderColor = PalavraoTheme.colors.primary,
            focusedBorderColor = PalavraoTheme.colors.primary,
            unfocusedBorderColor = PalavraoTheme.colors.primary
        ),
        singleLine = isSingleLine,
        enabled = enabled
    )
}

@Preview
@Composable
fun CustomWordTextFieldPreview() {
    PalavraoTheme {
        CustomWordTextField(
            text = "Palavra",
            hint = "Teste",
            error = "",
            isSingleLine = true,
            onValueChange = { },
        )
    }
}