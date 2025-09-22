// filepath: c:\Users\disne\AndroidStudioProjects\WhatsNextApp\app\src\main\java\com\example\whats_next_app\ui\screens\ProfileScreen.kt
package com.example.whats_next_app.ui.screens

// Layout and UI component imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll

// Material icons for UI actions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

// Material Design 3 components
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

// State management and composition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// UI layout and configuration
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavController

// Date handling
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen for managing user profile and preferences
 * Features:
 * - User's personal information
 * - Cancer diagnosis details
 * - App preferences (notifications, theme)
 * - Support information
 *
 * @param navController Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // State for user profile data
    // Currently empty - would be populated from database/preferences in real app
    val userProfile = remember {
        UserProfileData(
            name = "",
            email = "",
            cancerType = "",
            diagnosisDate = Date(),
            currentStage = ""
        )
    }

    // App preference states
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    // Main scaffold with top app bar
    Scaffold(
        topBar = {
            // App bar with back navigation and settings
            TopAppBar(
                title = { Text("Your Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Settings button
                    IconButton(onClick = { /* Open settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            // Scrollable content containing profile sections
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // User profile header with avatar
                ProfileHeader(profile = userProfile)

                Spacer(modifier = Modifier.height(24.dp))

                // Medical information section
                MedicalInfoCard(profile = userProfile)

                Spacer(modifier = Modifier.height(16.dp))

                // User preferences section
                PreferencesCard(
                    notificationsEnabled = notificationsEnabled,
                    onNotificationsChanged = { notificationsEnabled = it },
                    darkModeEnabled = darkModeEnabled,
                    onDarkModeChanged = { darkModeEnabled = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Support information section
                SupportCard()

                Spacer(modifier = Modifier.height(24.dp))

                // Sign out button
                Button(
                    onClick = { /* Sign out functionality */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Out")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Data class to store user profile information
 */
data class UserProfileData(
    val name: String,
    val email: String,
    val cancerType: String,
    val diagnosisDate: Date,
    val currentStage: String
)

/**
 * User profile header section with avatar and basic info
 *
 * @param profile User profile data to display
 */
@Composable
fun ProfileHeader(profile: UserProfileData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User avatar/icon
        Surface(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User name
        Text(
            text = profile.name,
            style = MaterialTheme.typography.headlineMedium
        )

        // User email
        Text(
            text = profile.email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Edit profile button
        Button(
            onClick = { /* Edit profile functionality */ }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Edit Profile")
        }
    }
}

/**
 * Card displaying user's medical information related to cancer
 *
 * @param profile User profile containing medical information
 */
@Composable
fun MedicalInfoCard(profile: UserProfileData) {
    val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Section title
            Text(
                text = "Medical Information",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            // Cancer type row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cancer Type:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )

                Text(
                    text = profile.cancerType,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Diagnosis date row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Diagnosis Date:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )

                Text(
                    text = dateFormat.format(profile.diagnosisDate),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Current stage row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current Stage:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )

                Text(
                    text = profile.currentStage,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Card displaying app preferences with toggles
 *
 * @param notificationsEnabled Whether notifications are currently enabled
 * @param onNotificationsChanged Callback when notification setting changes
 * @param darkModeEnabled Whether dark mode is currently enabled
 * @param onDarkModeChanged Callback when dark mode setting changes
 */
@Composable
fun PreferencesCard(
    notificationsEnabled: Boolean,
    onNotificationsChanged: (Boolean) -> Unit,
    darkModeEnabled: Boolean,
    onDarkModeChanged: (Boolean) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Section title
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications toggle row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Notification icon
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Setting label
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Toggle switch
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = onNotificationsChanged
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dark mode toggle row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dark mode icon
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Setting label
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Toggle switch
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = onDarkModeChanged
                )
            }
        }
    }
}

/**
 * Card displaying support information and resources
 */
@Composable
fun SupportCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Section title
            Text(
                text = "Support",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            // Support description
            Text(
                text = "Need help with the app or have questions about your cancer journey?",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contact support button
            Button(
                onClick = { /* Contact support functionality */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contact Support")
            }
        }
    }
}
