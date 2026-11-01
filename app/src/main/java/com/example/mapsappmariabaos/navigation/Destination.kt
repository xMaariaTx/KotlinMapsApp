package com.example.mapsappmariabaos.navigation

import kotlinx.serialization.Serializable
sealed class Destination() {
    @Serializable
    object Map : Destination()
    @Serializable
    object List : Destination()
    @Serializable
    object Marker : Destination()

    @Serializable
    object Menu : Destination()
}
