package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val displayName = viewModel.getUserName()
    val userName = viewModel.getUserName()
    val location = viewModel.getLocation()
    val phoneNumber = viewModel.getPhoneNumber()

    Scaffold(
        topBar = {
            TopBar(title = "Profile")
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = LightGreen)
                        ) {
                            Text(
                                text = displayName[0].toString(),
                                fontFamily = metropolisFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 36.sp,
                                color = Color.White,
                                modifier = Modifier.padding(32.dp)
                            )
                        }
                    }
                }

                item {
                    ActionCard(
                        color = Green,
                        icon = Icons.Default.QrCodeScanner,
                        text = "Scan Cattle",
                        modifier = Modifier.padding(16.dp)
                    ) {
                        navController.navigate(CattleAppScreens.CattleScannerScreen.route)
                    }
                }

                item {
                    ActionCard(
                        color = LightGreen,
                        icon = Icons.Default.EditNote,
                        text = "Manual Registration",
                        modifier = Modifier.padding(16.dp)
                    ) {
                        navController.navigate(CattleAppScreens.CattleScannerScreen.route)
                    }
                }

                item {
                    Button(onClick = {
                        viewModel.logout()
                        navController.navigate(CattleAppScreens.LoginScreen.route) {
                            popUpTo(CattleAppScreens.HomeScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Text(text = "Logout")
                    }
                }

            }
        }
    }

}
