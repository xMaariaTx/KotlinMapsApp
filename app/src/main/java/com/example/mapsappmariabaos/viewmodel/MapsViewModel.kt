package com.example.mapsappmariabaos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.mapsappmariabaos.permissions.MapPermissionState
import com.example.mapsappmariabaos.permissions.PermissionStatus
import com.google.android.gms.maps.model.LatLng

class MapsViewModel: ViewModel() {
    // 1. Creamos la variable que guardará la posición (empezamos en null)
    private var _selectedLatLng = mutableStateOf<LatLng?>(null)
    val selectedLatLng: State<LatLng?> = _selectedLatLng

    // 2. Creamos la función que llamarás desde el LongClick
    fun setLatLng(latLng: LatLng) {
        _selectedLatLng.value = latLng
    }

    private val _uiState =
        mutableStateOf<MapPermissionState>(MapPermissionState.Requesting)
    val uiState: State<MapPermissionState> = _uiState

    fun onPermissionResult(status: PermissionStatus) {
        _uiState.value = when (status) {
            PermissionStatus.Granted -> MapPermissionState.NavigateToMap
            PermissionStatus.Denied -> MapPermissionState.ShowDenied
            PermissionStatus.PermanentlyDenied -> MapPermissionState.ShowPermanentlyDenied
            PermissionStatus.Unknown -> MapPermissionState.Requesting
        }
    }
}