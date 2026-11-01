package com.example.mapsappmariabaos.screens

import android.R.attr.id
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mapsappmariabaos.viewmodel.AppViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController, viewModel: AppViewModel) {
    // Observamos la lista de marcadores desde el ViewModel
    val marcadores by viewModel.tasques.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Lista de Marcadores",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold)
            })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                items = marcadores,
                // Usamos el ID como key, o el hashcode si es nulo para evitar duplicados
                key = { it.id ?: it.hashCode() }
            ) { marcador ->

                // Configuración del estado del Swipe
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { valor ->
                        if (valor == SwipeToDismissBoxValue.EndToStart) {
                            marcador.id?.let { idMarcador ->
                                viewModel.esborrarTascaPerId(idMarcador)
                            }
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                            Color.Red else Color.Transparent

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Eliminar",
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    // Contenido de la tarjeta
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        onClick = {
                            navController.navigate("detail/${marcador.id}")
                        }
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = marcador.name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = marcador.description ?: "Sin descripción",
                                    color = Color.Gray
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = Color(0xFFE91E63)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}