package com.example.whats_next_app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Custom shapes for the What's Next App
// Using more rounded corners for a softer, friendlier appearance
val Shapes = Shapes(
    // Small components like buttons, chips
    small = RoundedCornerShape(8.dp),

    // Medium components like cards, dialogs
    medium = RoundedCornerShape(12.dp),

    // Large components like bottom sheets
    large = RoundedCornerShape(16.dp),

    // Extra large components
    extraLarge = RoundedCornerShape(24.dp)
)
