package com.cezila.core_designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.R

@Immutable
data class PalavraoBackground(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
) {
    companion object {
        @Composable
        fun defaultBackground(): PalavraoBackground {
            return PalavraoBackground(
                color = colorResource(id = R.color.pv_background_dark),
                tonalElevation = 0.dp,
            )
        }
    }
}

val LocalBackgroundTheme: ProvidableCompositionLocal<PalavraoBackground> =
    staticCompositionLocalOf { PalavraoBackground() }
