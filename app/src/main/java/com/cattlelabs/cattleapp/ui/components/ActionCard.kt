package com.cattlelabs.cattleapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cattlelabs.cattleapp.ui.theme.Green
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    color: Color = Green,
    icon: ImageVector,
    text: String,
    onCardClick: () -> Unit
) {
    Card(
        onClick = onCardClick,
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .border(width = 2.dp, color = Green, shape = RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier
                .background(color = color, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
                .fillMaxSize()
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text = text,
                fontFamily = metropolisFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}