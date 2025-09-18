package com.example.cattleapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.lang.reflect.Modifier

// a tiny helper to start mock scan and navigate to results
@Composable
fun ScanPlaceholderScreen(navController: NavController, viewModel: com.example.cattleapp.viewmodel.MainViewModel) {

        Text("Press Scan to simulate camera capture")

        Button(onClick = {
            viewModel.scanImageMock("dummy.jpg")
            navController.navigate("results")
        }) {
            Text("Scan Now")
        }
    }
