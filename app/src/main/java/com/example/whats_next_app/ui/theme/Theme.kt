package com.example.whats_next_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DeepBlue,
    secondary = DarkSage,
    tertiary = WarmTaupe,
    background = DarkBackground,
    surface = DarkSurface,
    error = MutedPeach,
    onPrimary = SoftSurface,
    onSecondary = SoftSurface,
    onTertiary = SoftSurface,
    onBackground = SoftSurface,
    onSurface = SoftSurface,
    onError = SoftSurface,
    primaryContainer = DeepBlue.copy(alpha = 0.7f),
    secondaryContainer = DarkSage.copy(alpha = 0.7f),
    tertiaryContainer = WarmTaupe.copy(alpha = 0.7f),
    errorContainer = MutedPeach.copy(alpha = 0.7f)
)

private val LightColorScheme = lightColorScheme(
    primary = CalmBlue,
    secondary = SoftSage,
    tertiary = WarmSand,
    background = SoftBackground,
    surface = SoftSurface,
    error = PeachAccent,
    onPrimary = SoftSurface,
    onSecondary = SoftSurface,
    onTertiary = DarkBackground,
    onBackground = DarkBackground,
    onSurface = DarkBackground,
    onError = SoftSurface,
    primaryContainer = CalmBlue.copy(alpha = 0.15f),
    secondaryContainer = SoftSage.copy(alpha = 0.15f),
    tertiaryContainer = WarmSand.copy(alpha = 0.3f),
    errorContainer = PeachAccent.copy(alpha = 0.15f)
)

@Composable
fun WhatsNextAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Update status bar color to match our theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes, // Apply our custom shapes
        content = content
    )
}