package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.data.model.Cattle
import com.cattlelabs.cattleapp.navigation.BottomNavOptions
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.core.BottomNavBar
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastRecordsScreen(
    navController: NavController,
    viewModel: CattleViewModel = hiltViewModel()
) {
    val cattleListState by viewModel.cattleListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCattle()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Past Records") })
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                bottomMenu = BottomNavOptions.bottomNavOptions
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = cattleListState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text("You haven't added any cattle records yet.")
                    } else {
                        CattleList(cattleRecords = state.data)
                    }
                }

                is UiState.NoDataFound -> {
                    Text("You haven't added any cattle records yet.")
                }

                is UiState.Failed -> {
                    ErrorState(message = state.message) { viewModel.getCattle() }
                }

                is UiState.InternetError -> {
                    ErrorState(message = "Please check your internet connection.") { viewModel.getCattle() }
                }

                is UiState.InternalServerError -> {
                    ErrorState(message = "A server error occurred.") { viewModel.getCattle() }
                }

                is UiState.Idle -> {
                    // The initial state before the API call is made
                }
            }
        }
    }
}

@Composable
fun CattleList(cattleRecords: List<Cattle>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cattleRecords) { cattle ->
            CattleRecordItem(cattle = cattle)
        }
    }
}

@Composable
fun CattleRecordItem(cattle: Cattle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Adds space between each Text
        ) {
            Text(
                text = cattle.name ?: "Unnamed",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tag: ${cattle.tagNumber}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Breed: ${cattle.breedName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Species: ${cattle.species}",
                style = MaterialTheme.typography.bodyMedium
            )
            // --- ADDED FIELDS ---
            cattle.sex?.let {
                Text(
                    text = "Sex: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            cattle.dob?.let {
                Text(
                    text = "DOB: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            cattle.taggingDate?.let {
                Text(
                    text = "Tagging Date: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            cattle.dataEntryDate?.let {
                Text(
                    text = "Entry Date: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = message, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}