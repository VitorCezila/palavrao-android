package com.cezila.core_designsystem

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

val currentSnackbarHostState: SnackbarHostState
    @Composable
    get() = LocalSnackbarHostState.current