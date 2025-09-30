@file:OptIn(ExperimentalComposeUiApi::class)

package com.cezila.core_designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

private val LocalColors = compositionLocalOf<PalavraoColors> {
    error("No colors provided! Make sure to wrap all usages of Palavrao components in PalavraoTheme.")
}

@Composable
fun PalavraoTheme(
    background: PalavraoBackground = PalavraoBackground.defaultBackground(),
    colors: PalavraoColors = PalavraoColors.defaultDarkColors(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalBackgroundTheme provides background,
    ) {
        Box(
            modifier = Modifier
                .background(background.color)
                .semantics { testTagsAsResourceId = true },
        ) {
            content()
        }
    }
}

public object PalavraoTheme {
    /**
     * Retrieves the current [PalavraoColors] at the call site's position in the hierarchy.
     */
    public val colors: PalavraoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    /**
     * Retrieves the current [PalavraoBackground] at the call site's position in the hierarchy.
     */
    public val background: PalavraoBackground
        @Composable
        @ReadOnlyComposable
        get() = LocalBackgroundTheme.current
}
