package com.example.mapsappmariabaos.permissions

import android.Manifest
sealed class AppPermission(val permissions: List<String>) {
    object Location : AppPermission(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )
    object CameraAndAudio : AppPermission(
        listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    )
}
