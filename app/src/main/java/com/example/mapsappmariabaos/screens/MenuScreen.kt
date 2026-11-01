package com.example.mapsappmariabaos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mapsappmariabaos.viewmodel.AppViewModel

@Composable
fun MenuScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AppViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título Principal
            Text(
                text = "APP MAPS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp, // Espaciado entre letras para toque moderno
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo destacado en Negro
            Text(
                text = "Map: World",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black, // Extra negrita
                color = Color.Black, // Color negro corregido
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Una línea decorativa
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxSize(0.1f) // Ocupa el 10% del ancho
                    .background(Color.Black)
            )
        }
    }
}