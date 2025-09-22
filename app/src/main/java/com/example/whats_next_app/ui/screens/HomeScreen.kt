package com.example.whats_next_app.ui.screens

// Layout imports for UI composition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn

// Material icon imports for UI elements
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Route

// Material design component imports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

// State management imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// UI configuration imports
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Navigation imports
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// App-specific imports
import com.example.whats_next_app.navigation.Screen
import com.example.whats_next_app.ui.theme.WhatsNextAppTheme

// Date formatting imports
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Main home screen of the What's Next app.
 * This is the central hub that displays all main features of the app including:
 * - Welcome section with date and user info
 * - Cancer journey progress
 * - Upcoming appointments
 * - Medication reminders
 * - Resource access
 * - Inspirational quotes
 * - Emergency contacts access
 *
 * @param navController Navigation controller to handle screen transitions
 */
@Composable
fun HomeScreen(navController: NavController) {
    // State for controlling the add action dialog visibility
    var showAddDialog by remember { mutableStateOf(false) }
    // State for tracking which action was selected from a card
    var selectedAction by remember { mutableStateOf("") }

    // State for appointments and medications lists
    // Empty by default - will be populated when user adds items
    val appointments = remember { mutableStateOf(emptyList<String>()) }
    val medications = remember { mutableStateOf(emptyList<String>()) }

    // Main scaffold layout with floating action button
    Scaffold(
        floatingActionButton = {
            // Global add button that shows options dialog
            FloatingActionButton(
                onClick = { showAddDialog = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Add") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        // Main content area
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            // Scrollable content with cards for different features
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Welcome card with date and profile access
                item {
                    WelcomeCard(onProfileClick = { navController.navigate(Screen.Profile.route) })
                }

                // Cancer journey progress tracking
                item {
                    JourneyProgressCard(
                        stage = "Getting Started",
                        onClick = { navController.navigate(Screen.JourneyGuide.route) }
                    )
                }

                // Section header for upcoming items
                item {
                    Text(
                        text = "Your Upcoming",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                // Upcoming appointments card
                item {
                    UpcomingAppointmentsCard(
                        appointments = appointments.value,
                        onClick = { navController.navigate(Screen.AppointmentTracker.route) },
                        onAppointmentClick = { appointment ->
                            selectedAction = "View appointment: $appointment"
                            showAddDialog = true
                        }
                    )
                }

                // Today's medications card
                item {
                    UpcomingMedicationsCard(
                        medications = medications.value,
                        onClick = { navController.navigate(Screen.MedicationReminders.route) },
                        onMedicationClick = { medication ->
                            selectedAction = "Take medication: $medication"
                            showAddDialog = true
                        }
                    )
                }

                // Resources and education access
                item {
                    ResourcesCard(
                        onClick = { navController.navigate(Screen.ResourceLibrary.route) }
                    )
                }

                // Inspirational quote card
                item {
                    DailyQuoteCard(
                        quote = "Every journey begins with a single step. We're here to help you take yours.",
                        author = "Dr. Lisa Baker, \"What's Next\""
                    )
                }

                // Emergency contacts button (highlighted with error colors for visibility)
                item {
                    EmergencyContactButton(
                        onClick = { navController.navigate(Screen.EmergencyContacts.route) }
                    )
                }
            }
        }
    }

    // Show action dialog when the add button is clicked or an item is selected
    if (showAddDialog) {
        ActionDialog(
            // Dialog title and message change based on whether an action was selected
            title = if (selectedAction.isEmpty()) "What would you like to add?" else "Action",
            message = if (selectedAction.isEmpty()) "Choose an option below" else selectedAction,
            onDismiss = { showAddDialog = false },
            onAppointment = {
                navController.navigate(Screen.AppointmentTracker.route)
                showAddDialog = false
            },
            onMedication = {
                navController.navigate(Screen.MedicationReminders.route)
                showAddDialog = false
            },
            onJournal = {
                navController.navigate(Screen.Journal.route)
                showAddDialog = false
            },
            // Only show options when no specific action was selected
            showOptions = selectedAction.isEmpty()
        )
    }
}

/**
 * Dialog that allows users to choose which type of item to add or view
 *
 * @param title Dialog title
 * @param message Dialog message or description
 * @param onDismiss Callback when dialog is dismissed
 * @param onAppointment Callback when appointment option is selected
 * @param onMedication Callback when medication option is selected
 * @param onJournal Callback when journal option is selected
 * @param showOptions Whether to show all options (true) or just OK button (false)
 */
@Composable
fun ActionDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onAppointment: () -> Unit,
    onMedication: () -> Unit,
    onJournal: () -> Unit,
    showOptions: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            if (showOptions) {
                // Show all action options if no specific action was selected
                Column {
                    TextButton(onClick = onAppointment) {
                        Text("Add Appointment")
                    }
                    TextButton(onClick = onMedication) {
                        Text("Add Medication")
                    }
                    TextButton(onClick = onJournal) {
                        Text("Add Journal Entry")
                    }
                }
            } else {
                // Just show OK button if viewing a specific item
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Welcome card that displays at the top of the home screen
 * Shows app title, current date, welcome message, and profile access
 * Also handles the first-time user welcome dialog
 *
 * @param onProfileClick Callback when profile button is clicked
 */
@Composable
fun WelcomeCard(onProfileClick: () -> Unit) {
    // State for username (currently not populated)
    val username = remember { mutableStateOf("") }

    // Use SharedPreferences to track if the welcome dialog has been shown before
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("WhatsNextApp", 0) }
    val hasSeenWelcomeDialog = remember { prefs.getBoolean("has_seen_welcome_dialog", false) }

    // Only show welcome dialog if user hasn't seen it before
    val showWelcomeDialog = remember { mutableStateOf(!hasSeenWelcomeDialog) }

    // Main welcome card
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App title
            Text(
                text = "What's Next",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Current date
            val date = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome message and profile access
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Personalized welcome or generic welcome
                Text(
                    text = if (username.value.isBlank()) "Welcome to your journey" else "Welcome, ${username.value}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                // Profile button
                TextButton(onClick = onProfileClick) {
                    Text("Profile")
                }
            }
        }
    }

    // First-time user welcome dialog with app explanation
    if (showWelcomeDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Save that user has seen the welcome dialog
                prefs.edit().putBoolean("has_seen_welcome_dialog", true).apply()
                showWelcomeDialog.value = false
            },
            title = { Text("Welcome to What's Next") },
            text = {
                Column {
                    // App introduction
                    Text(
                        "This companion app is based on Dr. Lisa Baker's book \"What's Next\" and is designed to help you navigate your cancer journey.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Feature summary
                    Text(
                        "You can use this app to:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text("• Track your appointments")
                    Text("• Manage your medications")
                    Text("• Keep a journal of your experiences")
                    Text("• Access resources and guidance")
                    Text("• Store emergency contacts")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Save that user has seen the welcome dialog
                        prefs.edit().putBoolean("has_seen_welcome_dialog", true).apply()
                        showWelcomeDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Get Started")
                }
            }
        )
    }
}

/**
 * Card that displays the user's cancer journey progress
 * Shows current stage and links to journey guidance
 *
 * @param stage The current stage in the cancer journey
 * @param onClick Callback when card is clicked to view journey guidance
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyProgressCard(stage: String, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Journey icon
            Icon(
                imageVector = Icons.Default.Route,
                contentDescription = "Journey",
                tint = MaterialTheme.colorScheme.secondary
            )

            // Journey information
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Your Cancer Journey",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "Current Stage: $stage",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "Tap to view guidance for this stage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

/**
 * Card that displays upcoming appointments
 * Shows a list of appointments or a message if none exist
 *
 * @param appointments List of appointment strings to display
 * @param onClick Callback when the card is clicked to go to appointments screen
 * @param onAppointmentClick Callback when a specific appointment is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingAppointmentsCard(
    appointments: List<String>,
    onClick: () -> Unit,
    onAppointmentClick: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card header with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Appointments",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Appointments",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show placeholder message if no appointments exist
            if (appointments.isEmpty()) {
                Text(
                    text = "No upcoming appointments. Tap the '+' button to add your first appointment.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // Display all appointments as clickable items
                appointments.forEachIndexed { index, appointment ->
                    if (index > 0) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    TextButton(
                        onClick = { onAppointmentClick(appointment) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = appointment,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Card that displays today's medications
 * Shows a list of medications or a message if none exist
 *
 * @param medications List of medication strings to display
 * @param onClick Callback when the card is clicked to go to medications screen
 * @param onMedicationClick Callback when a specific medication is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingMedicationsCard(
    medications: List<String>,
    onClick: () -> Unit,
    onMedicationClick: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card header with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Medication,
                    contentDescription = "Medications",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Today's Medications",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show placeholder message if no medications exist
            if (medications.isEmpty()) {
                Text(
                    text = "No medications scheduled for today. Tap the '+' button to add your first medication.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                // Display all medications as clickable items
                medications.forEachIndexed { index, medication ->
                    if (index > 0) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    TextButton(
                        onClick = { onMedicationClick(medication) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = medication,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Card that provides access to resources and education
 * Informs users about available articles, videos, and support groups
 *
 * @param onClick Callback when the card is clicked to go to resources screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesCard(onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card header with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Route,
                    contentDescription = "Resources",
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = "Resources & Education",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description of available resources
            Text(
                text = "Access helpful articles, videos, and support groups",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * Button that navigates to emergency contacts screen
 * Highlighted with error colors to indicate importance
 *
 * @param onClick Callback when the button is clicked
 */
@Composable
fun EmergencyContactButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Emergency",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Emergency Contacts",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Card that displays an inspirational quote of the day
 * Encourages and motivates users in their cancer journey
 *
 * @param quote The quote text to display
 * @param author The author of the quote
 */
@Composable
fun DailyQuoteCard(quote: String, author: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "— $author",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WhatsNextAppTheme {
        HomeScreen(rememberNavController())
    }
}
