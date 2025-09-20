package com.cattlelabs.cattleapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.navigation.CattleAppScreens
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.PredictionItemCard
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel

@Composable
fun BreedPredictionScreen(
    navController: NavController,
    encodedUri: String?,
    viewModel: CattleViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val predictionState by viewModel.predictionState.collectAsState()

    LaunchedEffect(key1 = encodedUri) {
        encodedUri?.let {
            val decodedUri = Uri.decode(it).toUri()
            viewModel.uploadAndPredict(context, decodedUri)
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Breed Prediction")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = predictionState) {
                is UiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Green)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Analyzing your image...",
                            fontFamily = metropolisFamily,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                is UiState.Success -> {
                    val data = state.data
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                "Prediction Results",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        items(data.predictions) { prediction ->

                            Log.d("BreedDebug", "Processing prediction. Breed: '${prediction.breed}', Breed ID: '${prediction.breedId}'")

                            PredictionItemCard(
                                breedId = prediction.breedId,
                                breed = prediction.breed,
                                accuracy = prediction.accuracy,
                                modifier = Modifier.fillMaxWidth(),
                                onCardClick = {
                                    prediction.breed?.let { breedName ->
                                        navController.navigate("${CattleAppScreens.CattleFormScreen.route}?breedName=$breedName")
                                    }
                                },
                                // ✅ This is the final connection you needed to make
                                onDetailsClick = {
                                    prediction.breedId?.let { id ->
                                        navController.navigate("${CattleAppScreens.BreedDetailScreen.route}/$id")
                                    }
                                }
                            )
                        }
                    }
                }

                is UiState.Failed -> {
                    Text("Error: ${state.message}")
                }

                is UiState.InternetError -> {
                    Text("Please check your internet connection.")
                }

                is UiState.InternalServerError -> {
                    Text("Server Error: ${state.errorMessage}")
                }

                else -> {
                    Text("Preparing to analyze...")
                }
            }
        }
    }
}