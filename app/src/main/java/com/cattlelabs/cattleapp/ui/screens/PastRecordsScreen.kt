package com.cattlelabs.cattleapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.cattlelabs.cattleapp.ui.theme.Green
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
                    title = stringResource(R.string.past_records_title),
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
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (val state = cattleListState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(color = Green)
                    }
                    is UiState.Success -> {
                        if (state.data.isEmpty()) {
                            MessageOnSurface(stringResource(R.string.past_records_empty))
                        } else {
                            CattleList(cattleRecords = state.data)
                        }
                    }
                    is UiState.NoDataFound -> {
                        MessageOnSurface(stringResource(R.string.past_records_empty))
                    }
                    is UiState.Failed -> {
                        ErrorState(message = state.message) { viewModel.getCattle() }
                    }
                    is UiState.InternetError -> {
                        ErrorState(message = stringResource(R.string.error_no_internet)) { viewModel.getCattle() }
                    }
                    is UiState.InternalServerError -> {
                        ErrorState(message = stringResource(R.string.error_server)) { viewModel.getCattle() }
                    }
                    is UiState.Idle -> { }
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = cattle.name ?: stringResource(R.string.past_records_unnamed),
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${stringResource(R.string.past_records_tag_label)}: ${cattle.tagNumber ?: "N/A"}",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.SemiBold,
                color = Green
            )
            Spacer(modifier = Modifier.height(8.dp))

            DetailItem(labelResId = R.string.past_records_breed_label, value = cattle.breedName ?: "Unknown")
            DetailItem(labelResId = R.string.past_records_species_label, value = cattle.species ?: "Unknown")
            cattle.sex?.let { DetailItem(labelResId = R.string.past_records_sex_label, value = it) }
            cattle.dob?.let { DetailItem(labelResId = R.string.past_records_dob_label, value = it) }
            cattle.taggingDate?.let { DetailItem(labelResId = R.string.past_records_tagging_date_label, value = it) }
            cattle.dataEntryDate?.let { DetailItem(labelResId = R.string.past_records_entry_date_label, value = it) }
        }
    }
}

@Composable
private fun DetailItem(@StringRes labelResId: Int, value: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append("${stringResource(id = labelResId)}: ")
            }
            append(value)
        },
        fontFamily = metropolisFamily,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Surface(
        modifier = Modifier.padding(32.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                textAlign = TextAlign.Center,
                fontFamily = metropolisFamily,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry_button), fontFamily = metropolisFamily, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MessageOnSurface(message: String) {
    Surface(
        modifier = Modifier.padding(32.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center,
            fontFamily = metropolisFamily,
            style = MaterialTheme.typography.titleMedium
        )
    }
}