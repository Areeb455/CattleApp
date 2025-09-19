package com.cattlelabs.cattleapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

sealed class BottomNavOptions(
    val route: String,
    val labelOfIcon: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val onOptionClicked: (NavController) -> Unit,
) {

    data object HomeScreen: BottomNavOptions(
        route = CattleAppScreens.HomeScreen.route,
        labelOfIcon = "Home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        onOptionClicked = {
            it.navigate(CattleAppScreens.HomeScreen.route) {
                popUpTo(it.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    )

    data object ProfileScreen: BottomNavOptions(
        route = CattleAppScreens.ProfileScreen.route,
        labelOfIcon = "Profile",
        unselectedIcon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person,
        onOptionClicked = {
            it.navigate(CattleAppScreens.ProfileScreen.route) {
                popUpTo(it.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    )

    companion object {
        val bottomNavOptions = listOf(
            HomeScreen,
            ProfileScreen
        )
    }

}