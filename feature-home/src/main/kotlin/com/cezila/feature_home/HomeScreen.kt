package com.cezila.feature_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cezila.core_designsystem.utils.pxToDp
import com.cezila.core_navigation.PalavraoScreen
import com.cezila.core_navigation.currentComposeNavigator

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val composeNavigator = currentComposeNavigator
    Box(modifier = modifier.padding(30.pxToDp())) {
        Button(onClick = {composeNavigator.navigate(PalavraoScreen.AvalancheScreen)}) {
            Text(text = "Ir para avalanche")
        }
    }

}