package com.example.mapsappmariabaos.permissions

sealed class MapPermissionState {
    object Requesting : MapPermissionState()
    object ShowDenied : MapPermissionState()
    object ShowPermanentlyDenied : MapPermissionState()
    object NavigateToMap : MapPermissionState()
}
