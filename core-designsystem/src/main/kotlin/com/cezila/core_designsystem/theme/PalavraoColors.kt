package com.cezila.core_designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.cezila.core_designsystem.R

@Immutable
data class PalavraoColors(
    val primary: Color,
    val onPrimary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val focus: Color,
    val correct: Color,
    val present: Color,
    val absent: Color
) {
    companion object {
        @Composable
        fun defaultDarkColors(): PalavraoColors = PalavraoColors(
            primary = colorResource(R.color.pv_primary_dark),
            onPrimary = colorResource(R.color.pv_onPrimary_dark),
            background = colorResource(R.color.pv_background_dark),
            onBackground = colorResource(R.color.pv_onBackground_dark),
            surface = colorResource(R.color.pv_surface_dark),
            onSurface = colorResource(R.color.pv_onSurface_dark),
            surfaceVariant = colorResource(R.color.pv_surfaceVariant_dark),
            outline = colorResource(R.color.pv_outline_dark),
            focus = colorResource(R.color.pv_focus_dark),
            correct = colorResource(R.color.pv_correct),
            present = colorResource(R.color.pv_present),
            absent = colorResource(R.color.pv_absent_dark)
        )
    }
}