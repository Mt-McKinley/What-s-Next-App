// filepath: c:\Users\disne\AndroidStudioProjects\WhatsNextApp\app\src\main\java\com\example\whats_next_app\ui\screens\AppointmentTrackerScreen.kt
package com.example.whats_next_app.ui.screens

// Layout and UI component imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// Material icons for UI actions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

// Material Design 3 components
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AlertDialog

// State management and composition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// UI layout and configuration
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavController

// App-specific models
import com.example.whats_next_app.model.Appointment

// Date handling
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen for managing medical appointments
 * Features:
 * - View list of upcoming appointments
 * - Add new appointments
 * - Edit existing appointments
 * - Delete appointments
 * - First-time user guidance
 *
 * @param navController Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentTrackerScreen(navController: NavController) {
    // Dialog visibility states
    var showAddAppointmentDialog by remember { mutableStateOf(false) }
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // State for storing the list of appointments
    // Empty by default until user adds appointments
    val appointments = remember {
        mutableStateOf(emptyList<Appointment>())
    }

    // Track if first-time guidance has been shown using SharedPreferences
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("WhatsNextApp", 0) }
    val hasSeenGuidance = remember { prefs.getBoolean("has_seen_appointment_guidance", false) }

    // Show guidance dialog for first-time users only when they have no appointments
    var showGuidanceDialog by remember { mutableStateOf(appointments.value.isEmpty() && !hasSeenGuidance) }

    // Main scaffold with top app bar and floating action button
    Scaffold(
        topBar = {
            // App bar with back navigation
            TopAppBar(
                title = { Text("Appointment Tracker") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // Button to add a new appointment
            ExtendedFloatingActionButton(
                onClick = {
                    selectedAppointment = null
                    showAddAppointmentDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Appointment") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            // Content displayed when there are no appointments
            if (appointments.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Icon indicating no appointments
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(72.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Title for the empty state
                    Text(
                        text = "No Appointments Yet",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Instruction text for adding the first appointment
                    Text(
                        text = "Keep track of all your medical appointments in one place. Tap the '+' button to add your first appointment.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Button to add the first appointment
                    Button(
                        onClick = {
                            selectedAppointment = null
                            showAddAppointmentDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Add First Appointment")
                    }
                }
            } else {
                // List of appointments
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appointments.value) { appointment ->
                        // Individual appointment card
                        AppointmentCard(
                            appointment = appointment,
                            onEdit = {
                                selectedAppointment = appointment
                                showAddAppointmentDialog = true
                            },
                            onDelete = {
                                selectedAppointment = appointment
                                showDeleteConfirmDialog = true
                            },
                            onClick = {
                                selectedAppointment = appointment
                            }
                        )
                    }
                }
            }
        }
    }

    // First-time user guidance dialog
    if (showGuidanceDialog) {
        AlertDialog(
            onDismissRequest = {
                // Save that user has seen the guidance
                prefs.edit().putBoolean("has_seen_appointment_guidance", true).apply()
                showGuidanceDialog = false
            },
            title = { Text("Track Your Appointments") },
            text = {
                Column {
                    Text(
                        "The Appointment Tracker helps you:",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("• Keep all your medical appointments in one place")
                    Text("• Set reminders for upcoming appointments")
                    Text("• Store important notes and questions for each visit")
                    Text("• Track your medical journey over time")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Get started by adding your first appointment using the '+' button.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Save that user has seen the guidance
                        prefs.edit().putBoolean("has_seen_appointment_guidance", true).apply()
                        showGuidanceDialog = false
                        showAddAppointmentDialog = true
                    }
                ) {
                    Text("Add Appointment")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Save that user has seen the guidance
                        prefs.edit().putBoolean("has_seen_appointment_guidance", true).apply()
                        showGuidanceDialog = false
                    }
                ) {
                    Text("Got it")
                }
            }
        )
    }

    // Show appointment details dialog when an appointment is selected
    if (selectedAppointment != null && !showAddAppointmentDialog && !showDeleteConfirmDialog) {
        AppointmentDetailDialog(
            appointment = selectedAppointment!!,
            onDismiss = { selectedAppointment = null },
            onEdit = {
                // Keep the selected appointment and show the edit dialog
                showAddAppointmentDialog = true
            }
        )
    }

    // Show add/edit appointment dialog
    if (showAddAppointmentDialog) {
        AddEditAppointmentDialog(
            appointment = selectedAppointment,
            onDismiss = {
                showAddAppointmentDialog = false
                // If we were editing, keep the details dialog open
                if (selectedAppointment != null && !showAddAppointmentDialog) {
                    selectedAppointment = null
                }
            },
            onSave = { newAppointment ->
                // Save the new or updated appointment
                val updatedList = appointments.value.toMutableList()
                if (selectedAppointment != null) {
                    // Update existing appointment
                    val index = updatedList.indexOfFirst { it.id == selectedAppointment!!.id }
                    if (index != -1) {
                        updatedList[index] = newAppointment
                    }
                } else {
                    // Add new appointment
                    updatedList.add(newAppointment.copy(id = updatedList.size + 1))
                }
                appointments.value = updatedList
                showAddAppointmentDialog = false
                selectedAppointment = null
            }
        )
    }

    // Show delete confirmation dialog
    if (showDeleteConfirmDialog && selectedAppointment != null) {
        DeleteAppointmentDialog(
            appointment = selectedAppointment!!,
            onDismiss = {
                showDeleteConfirmDialog = false
                selectedAppointment = null
            },
            onConfirmDelete = {
                // Delete the appointment from the list
                val updatedList = appointments.value.toMutableList()
                updatedList.remove(selectedAppointment)
                appointments.value = updatedList

                showDeleteConfirmDialog = false
                selectedAppointment = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: Appointment,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(appointment.dateTime))

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = appointment.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "With: ${appointment.doctorName}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Location: ${appointment.location}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (appointment.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${appointment.notes}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (appointment.questions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Questions to Ask:",
                    style = MaterialTheme.typography.bodyMedium
                )

                appointment.questions.forEach { question ->
                    Text(
                        text = "• $question",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentDetailDialog(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onEdit: () -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(appointment.dateTime))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(appointment.title) },
        text = {
            Column {
                Text(
                    text = "With: ${appointment.doctorName}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Location: ${appointment.location}",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (appointment.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Notes:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = appointment.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (appointment.questions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Questions to Ask:",
                        style = MaterialTheme.typography.titleSmall
                    )

                    appointment.questions.forEach { question ->
                        Row(
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = question,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onEdit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Edit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAppointmentDialog(
    appointment: Appointment?,
    onDismiss: () -> Unit,
    onSave: (Appointment) -> Unit
) {
    val isEditing = appointment != null
    val title = remember { mutableStateOf(appointment?.title ?: "") }
    val doctorName = remember { mutableStateOf(appointment?.doctorName ?: "") }
    val location = remember { mutableStateOf(appointment?.location ?: "") }
    val notes = remember { mutableStateOf(appointment?.notes ?: "") }
    val dateTime = remember { mutableStateOf(appointment?.dateTime ?: Date().time) }

    // In a real app, you would have date/time pickers for the date and time fields

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edit Appointment" else "Add New Appointment") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title field
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Oncology Consultation") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Doctor name field
                Text(
                    text = "Doctor/Provider",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = doctorName.value,
                    onValueChange = { doctorName.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Dr. Smith") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location field
                Text(
                    text = "Location",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = location.value,
                    onValueChange = { location.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Memorial Cancer Center - Room 305") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notes field
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = notes.value,
                    onValueChange = { notes.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Any special instructions or notes") },
                    minLines = 3
                )

                // In a complete implementation, you would add fields for:
                // - Date picker
                // - Time picker
                // - Question list with add/remove functionality
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // In a real app, you would validate the input and create/update the appointment
                    // Here we're just creating a simple placeholder appointment
                    val newAppointment = Appointment(
                        id = appointment?.id ?: 0,
                        title = title.value,
                        doctorName = doctorName.value,
                        dateTime = dateTime.value,
                        location = location.value,
                        notes = notes.value,
                        questions = appointment?.questions ?: listOf(),
                        isCompleted = appointment?.isCompleted ?: false
                    )
                    onSave(newAppointment)
                },
                enabled = title.value.isNotBlank() && doctorName.value.isNotBlank()
            ) {
                Text(if (isEditing) "Update" else "Save")
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
fun DeleteAppointmentDialog(
    appointment: Appointment,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete the appointment with ${appointment.doctorName}? This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
