package com.example.cattleapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.cattleapp.ui.screens.CattleManagementScreen
import com.example.cattleapp.ui.screens.DashboardScreen
import com.example.cattleapp.ui.screens.ProfileScreen
import com.example.cattleapp.ui.screens.UserDatabaseScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {

            // 1. NavigationBar background is now set to Black
            NavigationBar(
                containerColor = Color.Black
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Navigation Item for Home
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF3A3A3A) // Dark indicator for a subtle look
                    )
                )

                // Navigation Item for Database
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "database" } == true,
                    onClick = {
                        navController.navigate("database") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Database") },
                    icon = { Icon(Icons.Default.List, contentDescription = "Database") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF3A3A3A)
                    )
                )

                // Navigation Item for Profile
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF3A3A3A)
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { DashboardScreen(navController = navController) }
            composable("database") { UserDatabaseScreen() }
            composable("profile") { ProfileScreen() }

            // 2. Added the CattleManagementScreen route here
            // This ensures it displays within the Scaffold that has the bottom bar.
            composable("manage/{breedName}") { backStackEntry ->
                val breed = backStackEntry.arguments?.getString("breedName") ?: "Unknown"
                CattleManagementScreen(navController = navController, breedName = breed)
            }
        }
    }
}