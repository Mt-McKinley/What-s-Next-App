package com.example.whats_next_app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whats_next_app.R
import kotlinx.coroutines.delay

/**
 * A splash screen that displays your custom image with animation
 * before navigating to the main screen
 *
 * @param onSplashComplete Callback when splash animation is complete
 */
@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    // Animation values
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    // Get resource ID safely to avoid duplicate resource errors
    val context = LocalContext.current
    val imageResId = try {
        // Try to get your custom image resource
        context.resources.getIdentifier("splash_image", "drawable", context.packageName)
    } catch (e: Exception) {
        // If that fails, use the placeholder
        R.drawable.splash_image_placeholder
    }

    // Use a default if the resource ID is invalid
    val finalImageResId = if (imageResId != 0) imageResId else R.drawable.splash_image_placeholder

    // Trigger animations when the composable is first shown
    LaunchedEffect(key1 = true) {
        // Fade in and scale up
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )

        // Hold the splash screen for a moment
        delay(1000)

        // Navigate to the main screen
        onSplashComplete()
    }

    // Splash screen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // The main splash image - using the resource that was found
            Image(
                painter = painterResource(id = finalImageResId),
                contentDescription = "What's Next Logo",
                modifier = Modifier
                    .size(240.dp)
                    .alpha(alpha.value)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App name
            Text(
                text = "What's Next",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Your Cancer Journey Companion",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}
