package com.cattlelabs.cattleapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cattlelabs.cattleapp.data.Prefs
import com.cattlelabs.cattleapp.ui.screens.BreedDetailScreen
import com.cattlelabs.cattleapp.ui.screens.BreedPredictionScreen
import com.cattlelabs.cattleapp.ui.screens.CattleFormScreen
import com.cattlelabs.cattleapp.ui.screens.CattleScannerScreen
import com.cattlelabs.cattleapp.ui.screens.HomeScreen
import com.cattlelabs.cattleapp.ui.screens.LoginScreen
import com.cattlelabs.cattleapp.ui.screens.PastRecordsScreen
import com.cattlelabs.cattleapp.ui.screens.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val localContext = LocalContext.current
    val userId = Prefs(localContext).getUserId()
    val userName = Prefs(localContext).getUserName()

    val startDestination = if (!userId.isNullOrEmpty()) {
        CattleAppScreens.HomeScreen.route
    } else {
        CattleAppScreens.LoginScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = CattleAppScreens.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(CattleAppScreens.HomeScreen.route) {
                        popUpTo(CattleAppScreens.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = CattleAppScreens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(route = CattleAppScreens.PastRecordsScreen.route) {
            PastRecordsScreen(navController = navController)
        }

        composable(route = CattleAppScreens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }

        composable(route = CattleAppScreens.CattleScannerScreen.route) {
            CattleScannerScreen(navController = navController)
        }

        composable(
            route = "${CattleAppScreens.CattleFormScreen.route}?breedName={breedName}",
            arguments = listOf(
                navArgument("breedName") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val breedName = backStackEntry.arguments?.getString("breedName")
            CattleFormScreen(
                navController = navController,
                breedName = breedName
            )
        }

        composable(
            route = "${CattleAppScreens.BreedPredictionScreen.route}/{encodedUri}",
            arguments = listOf(
                navArgument("encodedUri") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("encodedUri")

            BreedPredictionScreen(
                navController = navController,
                encodedUri = encodedUri,
            )
        }

        composable(
            route = "${CattleAppScreens.BreedDetailScreen.route}/{breedId}",
            arguments = listOf(
                navArgument("breedId") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString("breedId")

            BreedDetailScreen(
                navController = navController,
                breedId = breedId,
            )
        }

    }
}