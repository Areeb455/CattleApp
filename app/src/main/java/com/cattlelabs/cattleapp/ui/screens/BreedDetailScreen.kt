package com.cattlelabs.cattleapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.data.model.BreedDetails
import com.cattlelabs.cattleapp.navigation.CattleAppScreens
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel

@Composable
fun BreedDetailScreen(
    navController: NavController,
    breedId: String?,
    viewModel: CattleViewModel = hiltViewModel()
) {
    val breedDetailsState = viewModel.breedDetailsState.collectAsState().value

    LaunchedEffect(key1 = breedId) {
        if (!breedId.isNullOrBlank()) {
            viewModel.getBreedById(breedId)
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Breed Details")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (breedId.isNullOrBlank()) {
                Text("No Breed ID provided.")
                return@Box
            }

            when (val state = breedDetailsState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    BreedDetailsContent(details = state.data)
                }

                is UiState.Failed -> {
                    Text(text = "Error: ${state.message}")
                }

                is UiState.InternetError -> {
                    Text(text = "Please check your internet connection.")
                }

                is UiState.InternalServerError -> {
                    Text(text = "Server Error: ${state.errorMessage}")
                }

                else -> {
                    // Idle or NoDataFound
                    Text(text = "Details not found for this breed.")
                }
            }
        }
    }
}

@Composable
private fun BreedDetailsContent(details: BreedDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = details.breedName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            DetailCard(label = "Species", value = details.species)
        }
        item {
            DetailCard(label = "Main Uses", value = details.mainUses)
        }
        item {
            DetailCard(label = "Breeding Tract", value = details.breedingTract)
        }
        item {
            DetailCard(label = "Location", value = details.location.joinToString(", "))
        }
        item {
            DetailCard(label = "Physical Description", value = details.physicalDesc)
        }
    }
}

@Composable
private fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}