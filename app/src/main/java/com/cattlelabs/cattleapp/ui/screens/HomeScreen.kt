package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.navigation.BottomNavOptions.Companion.bottomNavOptions
import com.cattlelabs.cattleapp.navigation.CattleAppScreens
import com.cattlelabs.cattleapp.ui.components.ActionCard
import com.cattlelabs.cattleapp.ui.components.core.BottomNavBar
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.LightGreen
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily
import com.cattlelabs.cattleapp.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val displayName = viewModel.getUserName()
    val location = viewModel.getLocation()

    Scaffold(
        topBar = {
            TopBar(title = " 👋  Hi, $displayName!")
        },
        bottomBar = {
            BottomNavBar(navController = navController, bottomMenu = bottomNavOptions)
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                        Text(
                            text = " $location",
                            fontFamily = metropolisFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item {
                    ActionCard(
                        color = Green,
                        icon = Icons.Default.QrCodeScanner,
                        text = "Scan Cattle",
                        modifier = Modifier.padding(16.dp)
                    ) {
                        navController.navigate(CattleAppScreens.CattleScanScreen.route)
                    }
                }

                item {
                    ActionCard(
                        color = LightGreen,
                        icon = Icons.Default.EditNote,
                        text = "Manual Registration",
                        modifier = Modifier.padding(16.dp)
                    ) {
                        navController.navigate(CattleAppScreens.CattleScanScreen.route)
                    }
                }

            }
        }
    }
}