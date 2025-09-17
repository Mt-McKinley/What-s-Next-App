package com.example.whats_next_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.whats_next_app.navigation.WhatsNextAppNavigation
import com.example.whats_next_app.ui.theme.WhatsNextAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhatsNextAppTheme {
                WhatsNextAppNavigation()
            }
        }
    }
}
