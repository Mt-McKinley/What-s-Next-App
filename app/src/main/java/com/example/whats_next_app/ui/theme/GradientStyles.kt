package com.example.whats_next_app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape

/**
 * Custom gradient components to create a warmer, more supportive visual environment
 */

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    brush: Brush,
    shape: Shape? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundModifier = if (shape != null) {
        modifier
            .clip(shape)
            .background(brush)
    } else {
        modifier.background(brush)
    }

    Box(
        modifier = backgroundModifier,
        content = content
    )
}

@Composable
fun PrimaryGradientBackground(
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface

    val gradient = Brush.verticalGradient(
        colors = listOf(
            primaryColor.copy(alpha = 0.1f),
            surfaceColor
        )
    )

    GradientBackground(
        modifier = modifier,
        brush = gradient,
        shape = shape,
        content = content
    )
}

@Composable
fun WarmGradientBackground(
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val surfaceColor = MaterialTheme.colorScheme.surface

    val gradient = Brush.verticalGradient(
        colors = listOf(
            tertiaryColor.copy(alpha = 0.1f),
            surfaceColor
        )
    )

    GradientBackground(
        modifier = modifier,
        brush = gradient,
        shape = shape,
        content = content
    )
}
