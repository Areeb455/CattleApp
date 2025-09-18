package com.example.cattleapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cattleapp.R
import com.example.cattleapp.viewmodel.BreedSuggestion

@Composable
fun BreedCard(
    suggestion: BreedSuggestion,
    modifier: Modifier = Modifier,
    onClick: (BreedSuggestion) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick(suggestion) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            // Image placeholder
            Image(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = suggestion.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)) {
                Text(
                    text = suggestion.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Confidence: ${(suggestion.confidence * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
