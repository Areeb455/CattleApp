package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
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
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // Fetching user details from the ViewModel
    val displayName = viewModel.getUserName()
    val location = viewModel.getLocation()
    val userID: String = viewModel.getCurrentUserId()?: "Guest"

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
            // Box to layer the background image
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.bg1),
                    contentDescription = "Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        // Header section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Profile initial with a border
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .border( // Added border
                                        width = 4.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp) // Padding between border and background
                                    .clip(CircleShape)
                                    .background(color = LightGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = displayName.firstOrNull()?.toString()?.uppercase() ?: "",
                                    fontFamily = metropolisFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 60.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                fontFamily = metropolisFamily
                            )
                        }
                    }

                    // User Details ActionCards

                    // Name card has been removed as requested

                    item {
                        ActionCard(
                            color = LightGreen,
                            icon = Icons.Default.Badge,
                            text = userID,
                            subtitle = userID,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {}
                    }

                    item {
                        ActionCard(
                            color = LightGreen,
                            icon = Icons.Default.LocationOn,
                            text = location,
                            subtitle = location,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {}
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        // Enhanced Logout Button
                        Button(
                            onClick = {
                                viewModel.logout()
                                navController.navigate(CattleAppScreens.LoginScreen.route) {
                                    popUpTo(CattleAppScreens.HomeScreen.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(55.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Logout, contentDescription = "Logout")
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text(
                                    text = "Logout",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = metropolisFamily
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}