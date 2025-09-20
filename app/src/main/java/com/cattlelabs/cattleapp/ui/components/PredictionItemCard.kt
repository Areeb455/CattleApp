package com.cattlelabs.cattleapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cattlelabs.cattleapp.ui.theme.LightGreen

@Composable
fun PredictionItemCard(
    breedId: String?,
    breed: String?,
    accuracy: Float?,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onDetailsClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = LightGreen
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column for Breed Name and "View Details" link
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = breed ?: "Unknown Breed",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                // Show "View Details" only if breedId is not null
                if (!breedId.isNullOrBlank()) {
                    Text(
                        text = "View Details",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { onDetailsClick() }
                    )
                }
            }

            // Accuracy Text
            Text(
                text = "%.2f%%".format(accuracy),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Arrow Icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Navigate to details"
            )
        }
    }
}