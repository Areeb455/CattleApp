package com.example.cattleapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cattleapp.ui.components.BreedCard
import com.example.cattleapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val suggestions by viewModel.breedSuggestions.collectAsState()
    val showGeo by viewModel.showGeoPopup.collectAsState()
    val geoSuggestedBreed by viewModel.geoSuggestedBreed.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Scan Results") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (suggestions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(74.dp), tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No breeds detected. Try again.", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { navController.navigate("scan") }) { Text("Try Again") }
                    }
                }
            } else {
                Text("Top Breed Suggestions", style = MaterialTheme.typography.titleLarge)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(suggestions) { s ->
                        BreedCard(suggestion = s, onClick = {
                            viewModel.selectBreed(s.name)
                            navController.navigate("manage/${s.name}")
                        })
                    }
                }
            }
        }
    }

    if (showGeo && geoSuggestedBreed != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissGeoPopup() },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissGeoPopup() }) {
                    Text("OK")
                }
            },
            title = { Text("Geospatial Confirmation") },
            text = { Text("Based on location, the likely breed is ${geoSuggestedBreed!!.name}") }
        )
    }
}
