package com.cattlelabs.cattleapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cattlelabs.cattleapp.data.Prefs
import com.cattlelabs.cattleapp.ui.screens.*

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

//        composable(route = CattleAppScreens.ProfileScreen.route) {
//            ProfileScreen(
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onLogoutClick = {
//                    Prefs(localContext).clearSession()
//                    navController.navigate(CattleAppScreens.LoginScreen.route) {
//                        popUpTo(0) {
//                            inclusive = true
//                        }
//                    }
//                }
//            )
//        }
//
//        // Fixed: Use CattleScannerScreen instead of AnimalScanScreen
//        composable(route = CattleAppScreens.AnimalScanScreen.route) {
//            CattleScannerScreen(
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onBreedSelected = { selectedBreed ->
//                    navController.navigate("${CattleAppScreens.AnimalFormScreen.route}/$selectedBreed") {
//                        popUpTo(CattleAppScreens.HomeScreen.route)
//                    }
//                }
//            )
//        }
//
//        composable(route = CattleAppScreens.AnimalFormScreen.route) {
//            CattleRegistrationForm(
//                preSelectedBreed = null,
//                onFormSubmit = {
//                    navController.navigate(CattleAppScreens.HomeScreen.route) {
//                        popUpTo(CattleAppScreens.HomeScreen.route) {
//                            inclusive = true
//                        }
//                    }
//                },
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable(route = "${CattleAppScreens.AnimalFormScreen.route}/{selectedBreed}") { backStackEntry ->
//            val selectedBreed = backStackEntry.arguments?.getString("selectedBreed") ?: ""
//            AnimalFormScreen(
//                preSelectedBreed = selectedBreed,
//                onFormSubmit = {
//                    navController.navigate(CattleAppScreens.HomeScreen.route) {
//                        popUpTo(CattleAppScreens.HomeScreen.route) {
//                            inclusive = true
//                        }
//                    }
//                },
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}
