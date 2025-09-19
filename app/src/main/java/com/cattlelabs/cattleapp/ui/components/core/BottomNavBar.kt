package com.cattlelabs.cattleapp.ui.components.core

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cattlelabs.cattleapp.navigation.BottomNavOptions
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.LightGreen

@Composable
fun BottomNavBar(
    navController: NavController,
    bottomMenu: List<BottomNavOptions>
) {
    NavigationBar {
        val backStackEntry = navController.currentBackStackEntryAsState()

        bottomMenu.forEach { menuItem ->
            val selected =
                (menuItem.route == backStackEntry.value?.destination?.parent?.route) ||
                        (menuItem.route == backStackEntry.value?.destination?.route)

            NavigationBarItem(
                selected = selected,
                onClick = { menuItem.onOptionClicked(navController) },
                icon = {
                    Icon(
                        imageVector = if (selected) menuItem.selectedIcon else menuItem.unselectedIcon,
                        contentDescription = menuItem.labelOfIcon,
                        tint = if (selected) Green else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = LightGreen,
                    selectedIconColor = Green,
                    unselectedIconColor = LightGreen
                )
            )
        }
    }
}
