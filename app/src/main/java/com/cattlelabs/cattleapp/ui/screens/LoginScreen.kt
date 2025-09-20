package com.cattlelabs.cattleapp.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.state.UiState
import com.cattlelabs.cattleapp.ui.components.CustomTextField // Import CustomTextField
import com.cattlelabs.cattleapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState = authViewModel.loginState.collectAsState().value

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onLoginSuccess()
        }
    }

    Box {

        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // User ID TextField using CustomTextField
            CustomTextField(
                value = userId,
                onValueChange = { userId = it },
                label = "User ID",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField using CustomTextField
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPasswordTextField = true // Enable password mode
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { authViewModel.login(userId, password) },
                enabled = userId.isNotEmpty() && password.isNotEmpty() && loginState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loginState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Login")
                }
            }

            // Error Message
            if (loginState is UiState.Failed) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = loginState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}