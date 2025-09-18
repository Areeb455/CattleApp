package com.example.cattleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cattleapp.ui.screens.*
import com.example.cattleapp.ui.theme.CattleAppTheme
import com.example.cattleapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
       CattleAppTheme  {
                val navController = rememberNavController()
                val vm: MainViewModel = viewModel()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController = navController, onLogin = { name, phone ->
                            vm.updateUser(name = name, phone = phone)
                        })
                    }

                    composable("home") {
                        // Use a root scaffold so bottom nav is persistent
                        MainScaffold(navController = navController, viewModel = vm)
                    }

                    // Individual screens (optional direct access)
                    composable("scan") {
                        ScanScreen(navController = navController, viewModel = vm)
                    }

                    composable("results") {
                        ScanResultScreen(navController = navController, viewModel = vm)
                    }

                    composable(
                        route = "manage/{breedName}",
                        arguments = listOf(navArgument("breedName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val breed = backStackEntry.arguments?.getString("breedName") ?: "Unknown"
                        CattleManagementScreen(navController = navController, breedName = breed)
                    }
                }
            }
        }
    }
}
