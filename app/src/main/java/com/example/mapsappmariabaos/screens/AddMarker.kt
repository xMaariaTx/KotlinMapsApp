package com.example.mapsappmariabaos.screens


import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mapsappmariabaos.permissions.AppPermission
import com.example.mapsappmariabaos.permissions.PermissionStatus
import com.example.mapsappmariabaos.permissions.rememberPermissionManager
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mapsappmariabaos.viewmodel.AppViewModel
import com.example.mapsappmariabaos.viewmodel.MapsViewModel
import com.example.mapsappmariabaos.viewmodel.MarkedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMarker(navController: NavHostController, markedViewModel: MarkedViewModel, mapsViewModel: MapsViewModel, appViewModel: AppViewModel, navigateToHome: () -> Unit) {
    val selectedLatLng by mapsViewModel.selectedLatLng
    val permissionManager = rememberPermissionManager(AppPermission.CameraAndAudio)
    val uiState by markedViewModel.uiState

    val context = LocalContext.current
    val imageUri by markedViewModel.imageUri
    val showDialog by markedViewModel.showDialog
    val isLoading by markedViewModel.isLoading
    val creationSuccess by markedViewModel.creationSuccess
    if (creationSuccess) {
        navigateToHome()
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) markedViewModel.onCameraImageSaved()
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { markedViewModel.setImageUri(it) }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }


    LaunchedEffect(selectedLatLng) {
        selectedLatLng?.let {
            latitude = it.latitude.toString()
            longitude = it.longitude.toString()
        }
    }

    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        markedViewModel.onPermissionResult(permissionManager.status)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { markedViewModel.changeShowDialog(false) },
            title = { Text("Selecciona una opción") },
            text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    markedViewModel.changeShowDialog(false)
                    val uri = com.example.mapsappmariabaos.utils.createImageUri(context)
                    uri?.let {
                        markedViewModel.tempUri = it
                        takePictureLauncher.launch(it)
                    }
                }) { Text("Tomar Foto") }
            },
            dismissButton = {
                TextButton(onClick = {
                    markedViewModel.changeShowDialog(false)
                    pickImageLauncher.launch("image/*")
                }) { Text("Elegir de Galería") }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuevo Marcador",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo: Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = {},
                    label = { Text("Latitud") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = {},
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripcion") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (imageUri == null) {
                IconButton(
                    onClick = { markedViewModel.changeShowDialog(true) },
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Foto",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black
                    )
                }
            } else {
                Box(contentAlignment = Alignment.TopEnd) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imageUri)
                            .crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { markedViewModel.setImageUri(null) },
                        modifier = Modifier
                            .offset(x = 10.dp, y = (-10).dp)
                            .size(30.dp)
                            .background(Color.Red, RoundedCornerShape(50))
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && latitude.isNotEmpty() && longitude.isNotEmpty()) {
                        appViewModel.viewModelScope.launch {
                        imageUri?.let { uri ->
                            val urlPublica = appViewModel.uploadImageToSupabase(context, uri)
                            appViewModel.afegirTasca(
                                titol = title,
                                desc = description,
                                coordlat = latitude.toFloat(), // Convertimos el String a Float
                                coordlog = longitude.toFloat(),
                                marked = false,
                                img = urlPublica
                            )
                            navController.popBackStack()
                        }
                    }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Add", color = Color.White, fontSize = 18.sp)
            }
        }

        // BOTÓN DE VOLVER ATRÁS
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}