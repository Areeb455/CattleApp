package com.example.cattleapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cattleapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CattleManagementScreen(
    navController: NavController,
    breedName: String
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Animal Management", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Changed from a gradient to a solid black background
                .background(Color.Black)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                // This is where your cow silhouette image would go
                Image(
                    painter = painterResource(id = R.drawable.img_1), // Assuming this is your silhouette
                    contentDescription = "Breed header",
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Selected: $breedName",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action cards now have borders and updated icons
            ManagementActionCard(title = "Health Monitoring", icon = Icons.Default.FavoriteBorder) { /*TODO*/ }
            ManagementActionCard(title = "Disease Control", icon = Icons.Default.Add) { /*TODO*/ }
            ManagementActionCard(title = "Nutrition Planning", icon = Icons.Default.Check) { /*TODO*/ }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("database") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Database", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Cattle Database", color = Color.White)
            }
        }
    }
}

@Composable
fun ManagementActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            // Added the border modifier here
            .border(
                BorderStroke(1.dp, Color(0xFF4CAF50)), // 1dp green border
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black // Black background blends with the screen
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}