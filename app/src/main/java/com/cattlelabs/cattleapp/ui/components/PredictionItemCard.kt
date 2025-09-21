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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cattlelabs.cattleapp.R
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.LightGreen
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily

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
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = breed ?: stringResource(R.string.past_records_unnamed),
                    fontFamily = metropolisFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (!breedId.isNullOrBlank() && breedId != "null") {
                    Text(
                        text = stringResource(R.string.prediction_view_details),
                        fontFamily = metropolisFamily,
                        color = Green,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { onDetailsClick() }
                    )
                }
            }

            val accuracyText = accuracy?.let { "%.2f%%".format(it) } ?: "-- %"

            Text(
                text = accuracyText,
                fontFamily = metropolisFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Green,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Navigate to details"
            )
        }
    }
}