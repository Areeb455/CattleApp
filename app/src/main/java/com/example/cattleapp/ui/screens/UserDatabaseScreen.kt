package com.example.cattleapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cattleapp.ui.components.CattleItem
import com.example.cattleapp.viewmodel.CattleRecord
import com.example.cattleapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDatabaseScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val list by viewModel.cattleRecords.collectAsState()

    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var nutrition by remember { mutableStateOf("") }
    var injuries by remember { mutableStateOf("") }
    var yield by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cattle Database", style = MaterialTheme.typography.headlineMedium, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(" Add New Entry", style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF4CAF50)))
            }

            // Form Fields inside a Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Inside your Column of OutlinedTextFields
                        val textFieldColors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color(0xFF4CAF50),   // Green border when focused
                            unfocusedIndicatorColor = Color(0xFF4CAF50), // Green border when unfocused
                            focusedLabelColor = Color(0xFF4CAF50),
                            unfocusedLabelColor = Color.Gray
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Cattle id") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = breed,
                            onValueChange = { breed = it },
                            label = { Text("User id") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = nutrition,
                            onValueChange = { nutrition = it },
                            label = { Text("Tag Number") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = injuries,
                            onValueChange = { injuries = it },
                            label = { Text("Species") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = breed,
                            onValueChange = { yield = it },
                            label = { Text("Breed") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors,
                            shape = RoundedCornerShape(12.dp)
                        )




                    }
                }
            }

            // Save Button
            item {
                Button(
                    onClick = {
                        if (name.isNotBlank() && breed.isNotBlank()) {
                            viewModel.addCattle(CattleRecord(name, breed, nutrition, injuries, yield))
                            name = ""; breed = ""; nutrition = ""; injuries = ""; yield = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Entry", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Entry", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }

            // Records Header
            item {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("📋 Saved Cattle Records", style = MaterialTheme.typography.titleLarge.copy(color = Color.White))
                }
            }

            // Display List
            if (list.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No saved cattle yet.", color = Color.Gray)
                    }
                }
            } else {
                items(list) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        CattleItem(record = item)
                    }
                }
            }
        }
    }
}

