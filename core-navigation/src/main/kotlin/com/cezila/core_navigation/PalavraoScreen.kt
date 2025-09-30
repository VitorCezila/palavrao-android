package com.cezila.core_navigation

import kotlinx.serialization.Serializable

sealed interface PalavraoScreen {

    @Serializable
    data object HomeScreen : PalavraoScreen

    @Serializable
    data object AvalancheScreen : PalavraoScreen

}
