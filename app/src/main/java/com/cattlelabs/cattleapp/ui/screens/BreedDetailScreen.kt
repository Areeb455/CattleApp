package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.data.model.BreedDetails
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel

@Composable
fun BreedDetailScreen(
    navController: NavController,
    breedId: String?,
    viewModel: CattleViewModel = hiltViewModel()
) {
    val breedDetailsState by viewModel.breedDetailsState.collectAsState()

    LaunchedEffect(key1 = breedId) {
        if (!breedId.isNullOrBlank() && viewModel.breedDetailsState.value is UiState.Idle) {
            viewModel.getBreedById(breedId)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg1),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 8.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(Color.Black.copy(alpha = 0.5f)) // Dark tint
                },
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    title = "Breed Details"
                )
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
                    ErrorContent(message = "No Breed ID provided.")
                    return@Box
                }

                when (val state = breedDetailsState) {
                    is UiState.Loading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Green)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading Details...",
                                fontFamily = metropolisFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    is UiState.Success -> {
                        BreedDetailsContent(details = state.data)
                    }

                    is UiState.Failed -> {
                        ErrorContent(message = state.message)
                    }

                    is UiState.InternetError -> {
                        ErrorContent(message = "Please check your internet connection.")
                    }

                    is UiState.InternalServerError -> {
                        ErrorContent(message = "A server error occurred.")
                    }

                    else -> {
                        Text(
                            text = "Details not found for this breed.",
                            fontFamily = metropolisFamily,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
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
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White // White for contrast against the dark background
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { DetailCard(label = "Species", value = details.species) }
        item { DetailCard(label = "Main Uses", value = details.mainUses) }
        item { DetailCard(label = "Breeding Tract", value = details.breedingTract) }
        item { DetailCard(label = "Location", value = details.location.joinToString(", ")) }
        item { DetailCard(label = "Physical Description", value = details.physicalDesc) }
    }
}

@Composable
private fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.Bold,
                color = Green // Use custom theme color for the label
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = metropolisFamily
            )
        }
    }
}

@Composable
private fun ErrorContent(message: String) {
    Surface(
        modifier = Modifier.padding(32.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error Icon",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                fontFamily = metropolisFamily,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}