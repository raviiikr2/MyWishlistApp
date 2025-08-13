package eu.com.mywishlistapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigate( innerPadding: PaddingValues,
    viewModel: WishViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home_screen"
    ) {
        composable(Screen.HomeScreen.route
        ) {
            HomeView(paddingValues = innerPadding,
                navController = navController,
                viewModel = viewModel)
        }
        composable(Screen.AddScreen.route
        ) {
            AppEditDetailView(id = 0L,
                viewModel = viewModel,
                navController = navController)
        }
    }

    }