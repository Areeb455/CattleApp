package com.cattlelabs.cattleapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.data.model.CattleRequest
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.core.TopBar
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily
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

    // --- Handle API call result ---
    LaunchedEffect(addCattleState) {
        when (val state = addCattleState) {
            is UiState.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Animal registered successfully!")
                }
                navController.popBackStack()
            }

            is UiState.Failed -> {
                coroutineScope.launch { snackbarHostState.showSnackbar("Error: ${state.message}") }
            }

            is UiState.InternetError -> {
                coroutineScope.launch { snackbarHostState.showSnackbar("Please check your internet connection.") }
            }

            else -> {}
        }
        if (addCattleState !is UiState.Idle && addCattleState !is UiState.Loading) {
            viewModel.resetAddCattleState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg1),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 8.dp) // Apply blur to the background
                .drawWithContent {
                    drawContent()
                    drawRect(Color.Black.copy(alpha = 0.4f)) // Dark tint for readability
                },
            contentScale = ContentScale.Crop,
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent, // Make scaffold transparent
            topBar = {
                TopBar(
                    title = "Cattle Registration"
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // --- Form Fields inside a Surface for contrast ---
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        FormTextField(
                            label = "Tag Number",
                            value = tagNumber,
                            onValueChange = { tagNumber = it })
                        FormDropdown(
                            label = "Species",
                            options = speciesOptions,
                            selectedValue = selectedSpecies,
                            onValueChange = { selectedSpecies = it },
                            expanded = speciesExpanded,
                            onExpandedChange = { speciesExpanded = it }
                        )
                        FormDropdown(
                            label = "Breed",
                            options = breedOptions,
                            selectedValue = selectedBreed,
                            onValueChange = { selectedBreed = it },
                            expanded = breedExpanded,
                            onExpandedChange = { breedExpanded = it }
                        )
                        FormTextField(label = "Name", value = name, onValueChange = { name = it })
                        FormDropdown(
                            label = "Gender",
                            options = genderOptions,
                            selectedValue = selectedGender,
                            onValueChange = { selectedGender = it },
                            expanded = genderExpanded,
                            onExpandedChange = { genderExpanded = it }
                        )
                        DateTextField(
                            label = "Date of Birth",
                            date = dob,
                            onDateChange = { dob = it })
                        DateTextField(
                            label = "Tagging Date",
                            date = taggingDate,
                            onDateChange = { taggingDate = it })
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Register Button ---
                val isLoading = addCattleState is UiState.Loading
                Button(
                    onClick = {
                        val userId = authViewModel.getCurrentUserId()
                        if (userId.isNullOrBlank()) {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Error: Could not identify user.") }
                            return@Button
                        }
                        val currentDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val request = CattleRequest(
                            userId = userId,
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
                    enabled = isFormValid && !isLoading,
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
                            fontFamily = metropolisFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontFamily = metropolisFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(fontFamily = metropolisFamily),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
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
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontFamily = metropolisFamily,
            fontWeight = FontWeight.SemiBold,
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
                textStyle = TextStyle(fontFamily = metropolisFamily),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontFamily = metropolisFamily) },
                        onClick = {
                            onValueChange(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateTextField(label: String, date: String, onDateChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontFamily = metropolisFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = onDateChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("YYYY-MM-DD", fontFamily = metropolisFamily) },
            trailingIcon = { Icon(Icons.Default.CalendarToday, "Date") },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(fontFamily = metropolisFamily),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}