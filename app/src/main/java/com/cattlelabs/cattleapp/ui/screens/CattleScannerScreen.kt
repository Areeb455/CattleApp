package com.cattlelabs.cattleapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cattlelabs.cattleapp.ui.components.CattleScanner
import kotlinx.coroutines.launch

sealed class CattleScannerScreenState {
    object Home : CattleScannerScreenState()
    object Camera : CattleScannerScreenState()
    data class Results(val imageUri: Uri) : CattleScannerScreenState()
    data class Processing(val imageUri: Uri) : CattleScannerScreenState()
}

@Composable
fun CattleScannerScreen() {
    var screenState =
        remember { mutableStateOf<CattleScannerScreenState>(CattleScannerScreenState.Home) }.value

    when (screenState) {
        is CattleScannerScreenState.Home -> {
            CattleScannerHomeScreen(
                onScanClick = {
                    screenState = CattleScannerScreenState.Camera
                }
            )
        }

        is CattleScannerScreenState.Camera -> {
            CattleScanner(
                onBackClick = {
                    screenState = CattleScannerScreenState.Home
                },
                onImageCaptured = { uri ->
                    screenState = CattleScannerScreenState.Processing(uri)
                    // Simulate processing delay
                    kotlinx.coroutines.MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        screenState = CattleScannerScreenState.Results(uri)
                    }
                },
                onImageSelected = { uri ->
                    screenState = CattleScannerScreenState.Processing(uri)
                    // Simulate processing delay
                    kotlinx.coroutines.MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        screenState = CattleScannerScreenState.Results(uri)
                    }
                }
            )
        }

        is CattleScannerScreenState.Processing -> {
            ProcessingScreen(
                imageUri = (screenState as CattleScannerScreenState.Processing).imageUri,
                onCancel = {
                    screenState = CattleScannerScreenState.Home
                }
            )
        }

        is CattleScannerScreenState.Results -> {
            ResultsScreen(
                imageUri = (screenState as CattleScannerScreenState.Results).imageUri,
                onBackToHome = {
                    screenState = CattleScannerScreenState.Home
                },
                onScanAnother = {
                    screenState = CattleScannerScreenState.Camera
                }
            )
        }
    }
}

@Composable
fun CattleScannerHomeScreen(
    onScanClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // App Title
        Text(
            text = "Cattle Scanner",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            textAlign = TextAlign.Center
        )

        Text(
            text = "AI-Powered Breed Identification",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Cattle Icon/Illustration
        Card(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF1F8E9)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Cattle",
                    modifier = Modifier.size(100.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Instructions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "How it works:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                InstructionStep(
                    number = "1",
                    text = "Take a photo of the cattle or select from gallery"
                )

                Spacer(modifier = Modifier.height(12.dp))

                InstructionStep(
                    number = "2",
                    text = "Our AI analyzes the image features"
                )

                Spacer(modifier = Modifier.height(12.dp))

                InstructionStep(
                    number = "3",
                    text = "Get instant breed identification results"
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Scan Button
        Button(
            onClick = onScanClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Start Scanning",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun InstructionStep(
    number: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    Color(0xFF4CAF50),
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ProcessingScreen(
    imageUri: Uri,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
            }

            Text(
                text = "Processing Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Image Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .clip(RoundedCornerShape(16.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(imageUri)
                        .build()
                ),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Processing Animation
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color(0xFF4CAF50),
            strokeWidth = 4.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Analyzing cattle breed...",
            fontSize = 18.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This may take a few seconds",
            fontSize = 14.sp,
            color = Color(0xFF999999),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ResultsScreen(
    imageUri: Uri,
    onBackToHome: () -> Unit,
    onScanAnother: () -> Unit
) {
    val context = LocalContext.current

    // Mock results - replace with actual AI results
    val mockResults = listOf(
        BreedResult("Holstein", 89.5f, "Most common dairy breed, known for high milk production"),
        BreedResult("Jersey", 7.3f, "Small dairy breed with high butterfat content"),
        BreedResult("Angus", 3.2f, "Popular beef breed, known for marbling")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToHome) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Scan Results",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            IconButton(onClick = { /* Share functionality */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share"
                )
            }
        }

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Image Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(imageUri)
                            .build()
                    ),
                    contentDescription = "Scanned Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Results Header
            Text(
                text = "Breed Identification Results",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Results List
            mockResults.forEach { result ->
                BreedResultItem(result = result)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackToHome,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Done")
                }

                Button(
                    onClick = onScanAnother,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Scan Another")
                }
            }
        }
    }
}

@Composable
fun BreedResultItem(result: BreedResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (result.confidence > 50f) Color(0xFFF1F8E9) else Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (result.confidence > 50f) BorderStroke(2.dp, Color(0xFF4CAF50)) else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.breedName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )

                Text(
                    text = "${result.confidence.toInt()}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (result.confidence > 50f) Color(0xFF4CAF50) else Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Confidence bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(result.confidence / 100f)
                        .height(8.dp)
                        .background(
                            if (result.confidence > 50f) Color(0xFF4CAF50) else Color(0xFF666666),
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result.description,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

data class BreedResult(
    val breedName: String,
    val confidence: Float,
    val description: String
)