package com.example.mapsappmariabaos.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@Serializable
data class MapDataBase @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int? = null,
    val name: String,
    val coordlat: Float,
    val coordlong: Float,
    val marked: Boolean = false,
    val description: String,
    val image: String? = null
)