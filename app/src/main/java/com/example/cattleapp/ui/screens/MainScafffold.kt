package com.example.cattleapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cattleapp.viewmodel.MainViewModel

@Composable
fun MainScaffold(navController: NavHostController, viewModel: MainViewModel) {
    val innerNav = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = innerNav.currentBackStackEntryAsState().value?.destination?.route == "dashboard",
                    onClick = { innerNav.navigate("dashboard") { popUpTo("dashboard") { inclusive = false } } },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { androidx.compose.material3.Text("Home") }
                )
                NavigationBarItem(
                    selected = innerNav.currentBackStackEntryAsState().value?.destination?.route == "database",
                    onClick = { innerNav.navigate("database") },
                    icon = { Icon(Icons.Default.List, contentDescription = "Database") },
                    label = { androidx.compose.material3.Text("Database") }
                )
                NavigationBarItem(
                    selected = innerNav.currentBackStackEntryAsState().value?.destination?.route == "profile",
                    onClick = { innerNav.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { androidx.compose.material3.Text("Profile") }
                )

            }
        }
    ) { padding ->
        NavHost(navController = innerNav, startDestination = "dashboard", modifier = Modifier.padding(padding)) {
            composable("dashboard") { DashboardScreen(navController = navController) }
//            composable("scan") { ScanPlaceHolderScreen (navController = navController, viewModel = viewModel) }
            composable("results") { ScanResultScreen2(navController = navController, viewModel = viewModel) }
            composable("database") { UserDatabaseScreen(navController = navController,viewModel = viewModel) }
            composable("profile") { ProfileScreen(viewModel = viewModel) }
            composable("manage/{breedName}") { backStackEntry ->
                val breed = backStackEntry.arguments?.getString("breedName") ?: "Unknown"
                CattleManagementScreen(navController = navController, breedName = breed)
            }
        }
    }
}
