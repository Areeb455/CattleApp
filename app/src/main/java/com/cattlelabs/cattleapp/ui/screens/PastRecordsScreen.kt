package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.data.model.Cattle
import com.cattlelabs.cattleapp.navigation.BottomNavOptions
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.core.BottomNavBar
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily
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
            TopBar(
                title = "Past Records"
            )
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

            Image(
                painter = painterResource(R.drawable.bg1),
                contentDescription = null,
                modifier = Modifier

                    .fillMaxSize(),
                contentScale = ContentScale.Crop,


                )

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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adjusted spacing for a cleaner look
        ) {
            // Cattle Name (Primary Title)
            Text(
                text = cattle.name ?: "Unnamed",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Tag Number (Secondary Title / Identifier)
            Text(
                text = "Tag: ${cattle.tagNumber}",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp)) // Separator

            // Other Details
            DetailItem(label = "Breed", value = cattle.breedName)
            DetailItem(label = "Species", value = cattle.species)

            cattle.sex?.let {
                DetailItem(label = "Sex", value = it)
            }
            cattle.dob?.let {
                DetailItem(label = "DOB", value = it)
            }
            cattle.taggingDate?.let {
                DetailItem(label = "Tagging Date", value = it)
            }
            cattle.dataEntryDate?.let {
                DetailItem(label = "Entry Date", value = it)
            }
        }
    }
}

/**
 * A helper composable to consistently style detail rows.
 * It bolds the label for clear separation.
 */
@Composable
private fun DetailItem(label: String, value: String) {
    Text(
        // Build a string with two different styles
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append("$label: ")
            }
            append(value)
        },
        fontFamily = metropolisFamily,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant // Slightly muted color for details
    )
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