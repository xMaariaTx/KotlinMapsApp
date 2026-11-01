package com.example.mapsappmariabaos.screens

import android.R.attr.fontWeight
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mapsappmariabaos.viewmodel.AppViewModel
import com.example.mapsappmariabaos.viewmodel.MarkedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, appViewModel: AppViewModel, markedViewModel: MarkedViewModel, marcadorId: Int) {
    // Obtenemos la lista y buscamos el marcador por ID
    val context = LocalContext.current
    val llistaTasques by appViewModel.tasques.collectAsState()

    // Buscamos el marcador específico
    val marcador = remember(marcadorId, llistaTasques) {
        llistaTasques.find { it.id == marcadorId }
    }

    // Estados para los textos
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    // Estados del ViewModel
    val imageUri by markedViewModel.imageUri
    val showDialog by markedViewModel.showDialog

    DisposableEffect(Unit) {
        onDispose {
            markedViewModel.setImageUri(null)
        }
    }

    // CARGA DE DATOS SEGURO
    LaunchedEffect(marcadorId) {
        markedViewModel.setImageUri(null) // Reset antes de cargar el nuevo
        marcador?.let {
            title = it.name
            description = it.description ?: ""
            latitude = it.coordlat.toString()
            longitude = it.coordlong.toString()

            if (!it.image.isNullOrEmpty()) {
                markedViewModel.setImageUri(Uri.parse(it.image))
            }
        }
    }

    // Lanzadores de Cámara y Galería
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { markedViewModel.changeShowDialog(false) },
            title = { Text("Añadir imagen") },
            text = { Text("¿Quieres tomar una foto o elegir una de la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    markedViewModel.changeShowDialog(false)
                    val uri = com.example.mapsappmariabaos.utils.createImageUri(context)
                    uri?.let {
                        markedViewModel.tempUri = it
                        takePictureLauncher.launch(it)
                    }
                }) { Text("Cámara") }
            },
            dismissButton = {
                TextButton(onClick = {
                    markedViewModel.changeShowDialog(false)
                    pickImageLauncher.launch("image/*")
                }) { Text("Galería") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalles del lugar",
                        fontSize = (20.sp),
                        fontWeight = FontWeight.Bold)},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        appViewModel.esborrarTascaPerId(marcadorId)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Nombre del marcador") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = {},
                    label = { Text("Latitud") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    shape = RoundedCornerShape(8.dp))
                OutlinedTextField(
                    value = longitude,
                    onValueChange = {},
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    shape = RoundedCornerShape(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (imageUri == null) {
                IconButton(
                    onClick = { markedViewModel.changeShowDialog(true) },
                    modifier = Modifier.size(80.dp).background(Color.LightGray.copy(0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = "Añadir",
                        modifier = Modifier.size(35.dp))
                }
            } else {
                Box(contentAlignment = Alignment.TopEnd) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(imageUri).crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { markedViewModel.setImageUri(null) },
                        modifier = Modifier.offset(x = 10.dp, y = (-10).dp).size(30.dp).background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Borrar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BOTÓN ACTUALIZAR
            Button(
                onClick = {
                    if (title.isNotEmpty()) {
                        appViewModel.viewModelScope.launch {
                            var finalImageUrl = marcador?.image // URL actual (la que ya había)

                            val currentUri = imageUri
                            if (currentUri != null) {
                                val uriString = currentUri.toString()

                                if (uriString.startsWith("content://") || uriString.startsWith("file://")) {
                                    val uploadedUrl = appViewModel.uploadImageToSupabase(context, currentUri)
                                    if (uploadedUrl != null) {
                                        finalImageUrl = uploadedUrl // Guardamos la nueva URL de internet
                                    }
                                }
                            } else {
                                // CASO C: El usuario borró la foto (imageUri es null)
                                finalImageUrl = null
                            }

                            // Ahora guardamos todo
                            appViewModel.editarTasca(marcadorId, title, description, finalImageUrl)

                            // Volvemos atrás
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Actualizar marcador", color = Color.White)
            }
        }
    }
}
