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
import com.example.cattleapp.ui.components.CattleItem
import com.example.cattleapp.viewmodel.CattleRecord
import com.example.cattleapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDatabaseScreen(
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
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // solid black background
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Add New Entry", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }

            // Form Fields
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val textFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color(0xFF4CAF50),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Cattle Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = breed,
                        onValueChange = { breed = it },
                        label = { Text("Breed") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = nutrition,
                        onValueChange = { nutrition = it },
                        label = { Text("Nutritional Needs") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = injuries,
                        onValueChange = { injuries = it },
                        label = { Text("Injuries/Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    OutlinedTextField(
                        value = yield,
                        onValueChange = { yield = it },
                        label = { Text("Milk Yield") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
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
                        .height(50.dp)
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Entry", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Entry", color = Color.White)
                }
            }

            // Divider and Header for the list
            item {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.DarkGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Saved Cattle Records", style = MaterialTheme.typography.titleLarge, color = Color.White)
                }
            }

            // Display List
            if (list.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No saved cattle yet.", color = Color.Gray)
                    }
                }
            } else {
                items(list) { item ->
                    CattleItem(record = item)
                }
            }
        }
    }
}
