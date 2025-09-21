package com.cattlelabs.cattleapp.ui.screens

import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.navigation.CattleAppScreens
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.PredictionItemCard
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily
import com.cattlelabs.cattleapp.viewmodel.AuthViewModel
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedPredictionScreen(
    navController: NavController,
    encodedUri: String?,
    viewModel: CattleViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val predictionState by viewModel.predictionState.collectAsState()
    val userLocation = authViewModel.getLocation()

    LaunchedEffect(key1 = encodedUri) {
        if (encodedUri != null && viewModel.predictionState.value is UiState.Idle) {
            val decodedUri = Uri.decode(encodedUri).toUri()
            viewModel.uploadAndPredict(context, decodedUri)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg1),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    title = stringResource(id = R.string.prediction_title)
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
                when (val state = predictionState) {
                    is UiState.Loading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Green)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.prediction_analyzing),
                                fontFamily = metropolisFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    is UiState.Success -> {
                        val data = state.data
                        val localMatches = data.predictions.filter { prediction ->
                            prediction.location.any { it.equals(userLocation, ignoreCase = true) }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = stringResource(R.string.prediction_top_results),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontFamily = metropolisFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            items(data.predictions) { prediction ->
                                PredictionItemCard(
                                    breedId = prediction.breedId,
                                    breed = prediction.breed,
                                    accuracy = prediction.accuracy,
                                    modifier = Modifier.fillMaxWidth(),
                                    onCardClick = {
                                        prediction.breed?.let { breedName ->
                                            val species = prediction.species
                                            var route =
                                                "${CattleAppScreens.CattleFormScreen.route}?breedName=$breedName"
                                            if (species != null) {
                                                route += "&species=$species"
                                            }
                                            navController.navigate(route)
                                        }
                                    },
                                    onDetailsClick = {
                                        prediction.breedId?.let { id ->
                                            navController.navigate("${CattleAppScreens.BreedDetailScreen.route}/$id")
                                        }
                                    }
                                )
                            }

                            if (localMatches.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    HorizontalDivider(color = Color.White.copy(alpha = 0.5f))
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = stringResource(R.string.prediction_local_results),
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontFamily = metropolisFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                items(localMatches) { prediction ->
                                    PredictionItemCard(
                                        breedId = prediction.breedId,
                                        breed = prediction.breed,
                                        accuracy = prediction.accuracy,
                                        modifier = Modifier.fillMaxWidth(),
                                        onCardClick = {
                                            prediction.breed?.let { breedName ->
                                                val species = prediction.species
                                                var route =
                                                    "${CattleAppScreens.CattleFormScreen.route}?breedName=$breedName"
                                                if (species != null) {
                                                    route += "&species=$species"
                                                }
                                                navController.navigate(route)
                                            }
                                        },
                                        onDetailsClick = {
                                            prediction.breedId?.let { id ->
                                                navController.navigate("${CattleAppScreens.BreedDetailScreen.route}/$id")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Failed -> {
                        ErrorContent(message = state.message)
                    }

                    is UiState.InternetError -> {
                        ErrorContent(message = stringResource(R.string.error_no_internet))
                    }

                    is UiState.InternalServerError -> {
                        ErrorContent(message = stringResource(R.string.error_server))
                    }

                    else -> {
                        Text(
                            text = stringResource(R.string.prediction_preparing),
                            fontFamily = metropolisFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
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
                imageVector = Icons.Default.CloudOff,
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