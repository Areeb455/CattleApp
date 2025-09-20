package com.cattlelabs.cattleapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cattlelabs.cattleapp.data.model.PredictionBody
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            val decodedUri = Uri.parse(Uri.decode(it))
            viewModel.uploadAndPredict(context, decodedUri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Breed Prediction") })
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
                    CircularProgressIndicator()
                    Text("Analyzing your image...", modifier = Modifier.padding(top = 100.dp))
                }

                is UiState.Success -> {
                    PredictionResult(data = state.data)
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
                    // Idle or NoDataFound state
                    Text("Preparing to analyze...")
                }
            }
        }
    }
}

@Composable
fun PredictionResult(data: PredictionBody) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Prediction Results", style = MaterialTheme.typography.headlineSmall)

        // Display the uploaded image
        AsyncImage(
            model = data.url,
            contentDescription = "Uploaded Cattle Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        // Display the list of predictions
        data.predictions.forEach { prediction ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = prediction.breed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "%.2f%%".format(prediction.accuracy),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}