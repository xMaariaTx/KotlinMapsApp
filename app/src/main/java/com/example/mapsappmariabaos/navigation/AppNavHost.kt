package com.example.mapsappmariabaos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsappmariabaos.layout.MainScaffold
import com.example.mapsappmariabaos.screens.AddMarker
import com.example.mapsappmariabaos.screens.ListScreen
import com.example.mapsappmariabaos.screens.MapsScreen
import com.example.mapsappmariabaos.viewmodel.AppViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mapsappmariabaos.screens.DetailScreen
import com.example.mapsappmariabaos.screens.MenuScreen
import com.example.mapsappmariabaos.viewmodel.MapsViewModel
import com.example.mapsappmariabaos.viewmodel.MarkedViewModel

@Composable
fun AppNavHost(navController: NavHostController, appViewModel: AppViewModel, mapsViewModel: MapsViewModel, markedViewModel: MarkedViewModel) {
    MainScaffold(navController) {
        NavHost(navController = navController, startDestination = Destination.Menu) {
            composable<Destination.Menu>
            {
                MenuScreen(navController = navController, viewModel = appViewModel)
            }
            composable<Destination.Map> { MapsScreen(Modifier, navController, mapsViewModel = mapsViewModel, appViewModel = appViewModel) }
            composable<Destination.List> { ListScreen(navController, appViewModel) }
            composable<Destination.Marker> { AddMarker(navController, markedViewModel = markedViewModel, mapsViewModel = mapsViewModel, appViewModel = appViewModel,
            navigateToHome = { navController.popBackStack()}) }
            composable(
                route = "detail/{marcadorId}",
                arguments = listOf(navArgument("marcadorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("marcadorId") ?: 0
                DetailScreen(navController, appViewModel, markedViewModel, id)
            }
        }
    }
}
