package com.cattlelabs.cattleapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cattlelabs.cattleapp.ui.theme.metropolisFamily // Assuming you have this theme file

/**
 * An enhanced, reusable text field with a transparent background,
 * password visibility toggle, and custom styling for dark UIs.
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPasswordTextField: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontFamily = metropolisFamily // Using your custom font
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Consistent rounded corners

        // Handles password visibility transformation
        visualTransformation = if (isPasswordTextField && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,

        // Sets the keyboard type to password if needed
        keyboardOptions = if (isPasswordTextField) KeyboardOptions(keyboardType = KeyboardType.Password) else keyboardOptions,

        // Adds the show/hide password icon
        trailingIcon = {
            if (isPasswordTextField) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }
            }
        },

        // Refined colors for a clean look on any background
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,     // No background fill when focused
            unfocusedContainerColor = Color.Transparent,   // No background fill when unfocused
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Black,           // Border color when focused
            unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f), // Border color when not focused
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray
        )
    )
}

