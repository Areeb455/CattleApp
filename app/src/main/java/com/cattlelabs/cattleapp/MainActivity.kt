package com.cattlelabs.cattleapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cattlelabs.cattleapp.navigation.AppNavigation
import com.cattlelabs.cattleapp.ui.theme.CattleAppTheme
import androidx.core.graphics.drawable.toDrawable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        enableEdgeToEdge()
        setContent {
            CattleAppTheme {
                AppNavigation()
            }
        }
    }
}