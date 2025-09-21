package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.navigation.BottomNavOptions
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
            TopBar(
                title = stringResource(R.string.home_greeting, displayName),
                actions = {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                bottomMenu = BottomNavOptions.bottomNavOptions
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.bg1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                            Text(
                                text = " $location",
                                fontFamily = metropolisFamily,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Spacer(modifier = Modifier.height(108.dp))
                    }
                    item {
                        ActionCard(
                            color = Green,
                            icon = Icons.Default.QrCodeScanner,
                            text = stringResource(R.string.home_scan_cattle),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            navController.navigate(CattleAppScreens.CattleScannerScreen.route)
                        }
                    }
                    item {
                        ActionCard(
                            color = LightGreen,
                            icon = Icons.Default.EditNote,
                            text = stringResource(R.string.home_manual_registration),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            navController.navigate(CattleAppScreens.CattleFormScreen.route)
                        }
                    }
                }
            }
        }
    }
}