package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.data.model.CattleRequest
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.viewmodel.AuthViewModel
import com.cattlelabs.cattleapp.viewmodel.CattleViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CattleFormScreen(
    navController: NavController,
    breedName: String?,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: CattleViewModel = hiltViewModel()
) {
    // --- State for API calls ---
    val addCattleState by viewModel.addCattleState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // --- Form Fields State ---
    var name by remember { mutableStateOf("") }
    var tagNumber by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedBreed by remember { mutableStateOf(breedName ?: "") }
    var selectedGender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var taggingDate by remember { mutableStateOf("") }

    // --- Dropdown Expanded State ---
    var speciesExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var breedExpanded by remember { mutableStateOf(false) }

    // --- Dropdown Options ---
    val speciesOptions = listOf("Cow", "Buffalo")
    val genderOptions = listOf("Male", "Female")
    val initialBreedOptions = listOf(
        "Alambadi", "Amritmahal", "Ayrshire", "Banni", "Bargur", "Bhadawari",
        "Brown Swiss", "Dangi", "Deoni", "Gir", "Guernsey", "Hallikar", "Hariana",
        "Holstein Friesian", "Jaffarabadi", "Jersey", "Kangayam", "Kankrej",
        "Kasaragod", "Kenkatha", "Kherigarh", "Khillari", "Krishna Valley",
        "Malnad Gidda", "Mehsana", "Murrah", "Nagori", "Nagpuri", "Nili Ravi",
        "Nimari", "Ongole", "Pulikulam", "Rathi", "Red Dane", "Red Sindhi",
        "Sahiwal", "Surti", "Tharparkar", "Toda", "Umblachery", "Vechur"
    )
    val breedOptions = remember(breedName) {
        if (!breedName.isNullOrBlank() && breedName !in initialBreedOptions) {
            listOf(breedName) + initialBreedOptions
        } else {
            initialBreedOptions
        }
    }

    // --- Form Validation ---
    val isFormValid by remember(tagNumber, selectedSpecies, selectedBreed) {
        derivedStateOf {
            tagNumber.isNotBlank() && selectedSpecies.isNotBlank() && selectedBreed.isNotBlank()
        }
    }

    LaunchedEffect(addCattleState) {
        when (val state = addCattleState) {
            is UiState.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Animal registered successfully!")
                }
                navController.popBackStack()
            }

            is UiState.Failed -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error: ${state.message}")
                }
            }

            is UiState.InternetError -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Please check your internet connection.")
                }
            }

            else -> {}
        }
        // Reset state after handling to prevent re-triggering
        if (addCattleState !is UiState.Idle && addCattleState !is UiState.Loading) {
            viewModel.resetAddCattleState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF0F2F5)
    ) { paddingValues ->

            Image(
                painter = painterResource(R.drawable.bg1),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Register Animal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(48.dp)) // Balance the IconButton
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {


                // --- Form Fields ---

                // Tag Number (Required)
                FormTextField(
                    label = "Tag Number*",
                    value = tagNumber,
                    onValueChange = { tagNumber = it })

                // Species (Required)
                FormDropdown(
                    label = "Species*",
                    options = speciesOptions,
                    selectedValue = selectedSpecies,
                    onValueChange = { selectedSpecies = it },
                    expanded = speciesExpanded,
                    onExpandedChange = { speciesExpanded = it }
                )

                // Breed (Required)
                FormDropdown(
                    label = "Breed*",
                    options = breedOptions,
                    selectedValue = selectedBreed,
                    onValueChange = { selectedBreed = it },
                    expanded = breedExpanded,
                    onExpandedChange = { breedExpanded = it }
                )

                // Animal Name (Optional)
                FormTextField(
                    label = "Name (Optional)",
                    value = name,
                    onValueChange = { name = it })

                // Gender (Optional)
                FormDropdown(
                    label = "Gender (Optional)",
                    options = genderOptions,
                    selectedValue = selectedGender,
                    onValueChange = { selectedGender = it },
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it }
                )

                // Date of Birth (Optional)
                DateTextField(
                    label = "Date of Birth (Optional)",
                    date = dob,
                    onDateChange = { dob = it })

                // Tagging Date (Optional)
                DateTextField(
                    label = "Tagging Date (Optional)",
                    date = taggingDate,
                    onDateChange = { taggingDate = it })


                Spacer(modifier = Modifier.height(32.dp))

                // --- Register Button ---
                val isLoading = addCattleState is UiState.Loading
                Button(
                    onClick = {
                        val currentDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val request = CattleRequest(
                            userId = authViewModel.getCurrentUserId(),
                            tagNumber = tagNumber,
                            species = selectedSpecies,
                            breed = selectedBreed,
                            name = name.takeIf { it.isNotBlank() },
                            sex = selectedGender.takeIf { it.isNotBlank() },
                            dob = dob.takeIf { it.isNotBlank() },
                            taggingDate = taggingDate.takeIf { it.isNotBlank() },
                            dataEntryDate = currentDate
                        )
                        viewModel.addCattle(request)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isFormValid && !isLoading, // Button disabled if form invalid or loading
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Register Animal",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}


// Helper Composables for clean code

@Composable
private fun FormTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormDropdown(
    label: String,
    options: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(!expanded) }) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        onValueChange(option)
                        onExpandedChange(false)
                    })
                }
            }
        }
    }
}

@Composable
private fun DateTextField(label: String, date: String, onDateChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = onDateChange, // Changed to be editable
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("YYYY-MM-DD") }, // Suggests the format
            trailingIcon = { Icon(Icons.Default.CalendarToday, "Date") },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Sets numeric keyboard
        )
    }
}