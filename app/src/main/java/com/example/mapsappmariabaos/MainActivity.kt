package com.example.mapsappmariabaos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.mapsappmariabaos.navigation.AppNavHost
import com.example.mapsappmariabaos.ui.theme.MapsAppMariaBaosTheme
import com.example.mapsappmariabaos.viewmodel.AppViewModel
import com.example.mapsappmariabaos.viewmodel.MapsViewModel
import com.example.mapsappmariabaos.viewmodel.MarkedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppMariaBaosTheme {
                val navController = rememberNavController()
                val appViewModel: AppViewModel by viewModels()
                val mapsViewModel: MapsViewModel by viewModels()
                val markedViewModel: MarkedViewModel by viewModels()
                AppNavHost(navController = navController, appViewModel = appViewModel, mapsViewModel = mapsViewModel, markedViewModel = markedViewModel)
            }
        }
    }
}