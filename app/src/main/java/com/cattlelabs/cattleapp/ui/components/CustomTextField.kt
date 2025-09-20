package com.cattlelabs.cattleapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPasswordTextField: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    color: Color = Color.Black,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontFamily = metropolisFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = color,
            unfocusedTextColor = color,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = color,
            focusedIndicatorColor = color,
            unfocusedIndicatorColor = color,
            focusedLabelColor = color,
            unfocusedLabelColor = color,
            cursorColor = color,
            disabledContainerColor = Color.Unspecified,
            disabledTextColor = Color.Unspecified
        ),
        visualTransformation = if (isPasswordTextField) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPasswordTextField) KeyboardOptions(keyboardType = KeyboardType.Password) else keyboardOptions,
        modifier = modifier.fillMaxWidth()
    )
}