package com.example.whats_next_app.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Route
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.whats_next_app.navigation.Screen
import com.example.whats_next_app.ui.theme.WhatsNextAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    WelcomeCard(onProfileClick = { navController.navigate(Screen.Profile.route) })
                }

                item {
                    JourneyProgressCard(
                        stage = "Diagnosis",
                        onClick = { navController.navigate(Screen.JourneyGuide.route) }
                    )
                }

                item {
                    Text(
                        text = "Your Upcoming",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                item {
                    UpcomingAppointmentsCard(
                        appointments = listOf(
                            "Oncologist - Dr. Smith - Sep 20",
                            "Blood Work - City Hospital - Sep 22"
                        ),
                        onClick = { navController.navigate(Screen.AppointmentTracker.route) },
                        onAppointmentClick = { appointment ->
                            // In a real app, you would navigate to the specific appointment
                            // For now we'll just show a dialog
                            selectedAction = "View appointment: $appointment"
                            showAddDialog = true
                        }
                    )
                }

                item {
                    UpcomingMedicationsCard(
                        medications = listOf(
                            "Paclitaxel - 1 tablet - 9:00 AM",
                            "Dexamethasone - 2 tablets - 8:00 PM"
                        ),
                        onClick = { navController.navigate(Screen.MedicationReminders.route) },
                        onMedicationClick = { medication ->
                            // In a real app, you would navigate to the specific medication
                            selectedAction = "Take medication: $medication"
                            showAddDialog = true
                        }
                    )
                }

                item {
                    ResourcesCard(
                        onClick = { navController.navigate(Screen.ResourceLibrary.route) }
                    )
                }

                item {
                    DailyQuoteCard(
                        quote = "You are stronger than you know, braver than you believe, and loved more than you can imagine.",
                        author = "Dr. Lisa Baker, \"What's Next\""
                    )
                }

                item {
                    EmergencyContactButton(
                        onClick = { navController.navigate(Screen.EmergencyContacts.route) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        ActionDialog(
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
            showOptions = selectedAction.isEmpty()
        )
    }
}

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

@Composable
fun WelcomeCard(onProfileClick: () -> Unit) {
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
            Text(
                text = "What's Next",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            val date = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome back, Sarah",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                TextButton(onClick = onProfileClick) {
                    Text("Profile")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyProgressCard(stage: String, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Route,
                contentDescription = "Journey",
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Your Cancer Journey",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Current Stage: $stage",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Tap to view guidance for this stage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

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
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (appointments.isEmpty()) {
                Text(
                    text = "No upcoming appointments",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            } else {
                appointments.forEachIndexed { index, appointment ->
                    if (index > 0) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    TextButton(
                        onClick = { onAppointmentClick(appointment) },
                        modifier = Modifier.fillMaxWidth()
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
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (medications.isEmpty()) {
                Text(
                    text = "No medications scheduled for today",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            } else {
                medications.forEachIndexed { index, medication ->
                    if (index > 0) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    TextButton(
                        onClick = { onMedicationClick(medication) },
                        modifier = Modifier.fillMaxWidth()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesCard(onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Route,
                    contentDescription = "Resources",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Resources & Education",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Access helpful articles, videos, and support groups",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

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
        Text(
            text = "Emergency Contacts",
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun DailyQuoteCard(quote: String, author: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "- $author",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSecondaryContainer
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
