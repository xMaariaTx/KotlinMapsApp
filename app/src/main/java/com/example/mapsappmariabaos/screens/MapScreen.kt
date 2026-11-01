package com.example.mapsappmariabaos.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mapsappmariabaos.permissions.AppPermission
import com.example.mapsappmariabaos.permissions.MapPermissionState
import com.example.mapsappmariabaos.permissions.PermissionContent
import com.example.mapsappmariabaos.permissions.PermissionStatus
import com.example.mapsappmariabaos.permissions.rememberPermissionManager
import com.example.mapsappmariabaos.viewmodel.AppViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mapsappmariabaos.navigation.Destination
import com.example.mapsappmariabaos.viewmodel.MapsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun MapsScreen(modifier: Modifier = Modifier, navController: NavHostController, mapsViewModel: MapsViewModel, appViewModel: AppViewModel) {
    val permissionManager = rememberPermissionManager(AppPermission.Location)
    val uiState by mapsViewModel.uiState
    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        mapsViewModel.onPermissionResult(permissionManager.status)
    }
    when (uiState) {
        MapPermissionState.NavigateToMap -> Column(modifier.fillMaxSize()) {
            val llistaTasques by appViewModel.tasques.collectAsState()

            //Cordenadas
            val londonBigBen = LatLng(51.5007292, -0.1246254)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(londonBigBen, 17f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    mapsViewModel.setLatLng(latLng)
                    navController.navigate(Destination.Marker)
                }
            ) {
                //Pintamos todos los marcadores que vienen de Supabase
                llistaTasques.forEach { tasca ->
                    Marker(
                        state = rememberMarkerState(
                            key = tasca.id.toString(),
                            position = LatLng(tasca.coordlat.toDouble(), tasca.coordlong.toDouble())),
                        title = tasca.name,
                        snippet = tasca.description
                    )
                }
            }
        }
        MapPermissionState.ShowDenied ->
        PermissionContent(PermissionStatus.Denied, permissionManager.requestPermissions)
        MapPermissionState.ShowPermanentlyDenied ->
            PermissionContent(PermissionStatus.PermanentlyDenied, {})
        MapPermissionState.Requesting ->
            PermissionContent(PermissionStatus.Unknown, permissionManager.requestPermissions)
    }

}

