package com.example.mapsappmariabaos.permissions

sealed class CreateMarkerPermissionState {
    object Requesting : CreateMarkerPermissionState()
    object ShowDenied : CreateMarkerPermissionState()
    object ShowPermanentlyDenied : CreateMarkerPermissionState()
    object NavigateToMap : CreateMarkerPermissionState()
}