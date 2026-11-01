package com.example.mapsappmariabaos.viewmodel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapsappmariabaos.permissions.CreateMarkerPermissionState
import com.example.mapsappmariabaos.permissions.PermissionStatus
import androidx.compose.runtime.State


class MarkedViewModel: ViewModel() {
    private val _uiStateMark =
        mutableStateOf<CreateMarkerPermissionState>(CreateMarkerPermissionState.Requesting)

    val uiState: MutableState<CreateMarkerPermissionState> = _uiStateMark

    fun onPermissionResult(status: PermissionStatus) {
        _uiStateMark.value = when (status) {
            PermissionStatus.Granted ->
                CreateMarkerPermissionState.NavigateToMap

            PermissionStatus.Denied ->
                CreateMarkerPermissionState.ShowDenied

            PermissionStatus.PermanentlyDenied ->
                CreateMarkerPermissionState.ShowPermanentlyDenied

            PermissionStatus.Unknown ->
                CreateMarkerPermissionState.Requesting
        }
    }

    //Camara
    private var _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri
    private var _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    private val _creationSuccess = mutableStateOf(false)
    val creationSuccess: State<Boolean> = _creationSuccess
    var tempUri: Uri? = null

    fun onCameraImageSaved() {
        _imageUri.value = tempUri
    }
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
    fun changeShowDialog(value: Boolean) {
        _showDialog.value = value
    }

}