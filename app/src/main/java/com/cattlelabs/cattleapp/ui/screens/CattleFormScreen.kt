package com.cattlelabs.cattleapp.ui.screens

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
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
    species: String?,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: CattleViewModel = hiltViewModel()
) {
    val addCattleState by viewModel.addCattleState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var tagNumber by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf(species ?: "") }
    var selectedBreed by remember { mutableStateOf(breedName ?: "") }
    var selectedGender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var taggingDate by remember { mutableStateOf("") }

    var speciesExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var breedExpanded by remember { mutableStateOf(false) }

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

    val isFormValid by remember(tagNumber, selectedSpecies, selectedBreed) {
        derivedStateOf {
            tagNumber.isNotBlank() && selectedSpecies.isNotBlank() && selectedBreed.isNotBlank()
        }
    }

    LaunchedEffect(addCattleState) {
        when (val state = addCattleState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar("Animal registered successfully!")
                navController.popBackStack()
                viewModel.resetAddCattleState()
            }

            is UiState.Failed -> {
                snackbarHostState.showSnackbar("Error: ${state.message}")
                viewModel.resetAddCattleState()
            }

            is UiState.InternetError -> {
                viewModel.resetAddCattleState()
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg1),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 8.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(Color.Black.copy(alpha = 0.4f))
                },
            contentScale = ContentScale.Crop,
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    title = stringResource(R.string.form_title)
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
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        FormTextField(
                            labelResId = R.string.form_tag_number,
                            value = tagNumber,
                            onValueChange = { tagNumber = it })
                        FormDropdown(
                            labelResId = R.string.form_species,
                            options = speciesOptions,
                            selectedValue = selectedSpecies,
                            onValueChange = { selectedSpecies = it },
                            expanded = speciesExpanded,
                            onExpandedChange = { speciesExpanded = it }
                        )
                        FormDropdown(
                            labelResId = R.string.form_breed,
                            options = breedOptions,
                            selectedValue = selectedBreed,
                            onValueChange = { selectedBreed = it },
                            expanded = breedExpanded,
                            onExpandedChange = { breedExpanded = it }
                        )
                        FormTextField(
                            labelResId = R.string.form_name,
                            value = name,
                            onValueChange = { name = it })
                        FormDropdown(
                            labelResId = R.string.form_gender,
                            options = genderOptions,
                            selectedValue = selectedGender,
                            onValueChange = { selectedGender = it },
                            expanded = genderExpanded,
                            onExpandedChange = { genderExpanded = it }
                        )
                        DateTextField(
                            labelResId = R.string.form_dob,
                            date = dob,
                            onDateChange = { dob = it })
                        DateTextField(
                            labelResId = R.string.form_tagging_date,
                            date = taggingDate,
                            onDateChange = { taggingDate = it })
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

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
                            text = stringResource(R.string.form_title),
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
private fun FormTextField(
    @StringRes labelResId: Int,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = stringResource(id = labelResId),
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
    @StringRes labelResId: Int,
    options: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = stringResource(id = labelResId),
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
private fun DateTextField(
    @StringRes labelResId: Int,
    date: String,
    onDateChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = stringResource(id = labelResId),
            style = MaterialTheme.typography.labelLarge,
            fontFamily = metropolisFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = onDateChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(R.string.form_date_placeholder),
                    fontFamily = metropolisFamily
                )
            },
            trailingIcon = { Icon(Icons.Default.CalendarToday, "Date") },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            textStyle = TextStyle(fontFamily = metropolisFamily),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}