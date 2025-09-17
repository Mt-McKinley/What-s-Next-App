package com.example.whats_next_app.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whats_next_app.model.Appointment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentTrackerScreen(navController: NavController) {
    var showAddAppointmentDialog by remember { mutableStateOf(false) }
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Convert to mutable state so we can update the list when deleting appointments
    val appointments = remember {
        mutableStateOf(
            listOf(
                Appointment(
                    id = 1,
                    title = "Oncology Consultation",
                    doctorName = "Dr. Smith",
                    dateTime = Date().time + 86400000, // Tomorrow
                    location = "Memorial Cancer Center - Room 305",
                    notes = "Bring recent test results and list of current medications",
                    questions = listOf("What are the treatment options?", "What side effects should I expect?"),
                    isCompleted = false
                ),
                Appointment(
                    id = 2,
                    title = "Radiation Therapy",
                    doctorName = "Dr. Johnson",
                    dateTime = Date().time + 4 * 86400000, // 4 days from now
                    location = "City Hospital - Radiation Oncology Dept",
                    notes = "First treatment session. Arrive 15 minutes early to complete paperwork.",
                    questions = listOf("How long will each session take?", "How many sessions will I need?"),
                    isCompleted = false
                ),
                Appointment(
                    id = 3,
                    title = "Blood Work",
                    doctorName = "Lab Technician",
                    dateTime = Date().time + 8 * 86400000, // 8 days from now
                    location = "Medical Laboratory - 2nd Floor",
                    notes = "Fasting required 8 hours before appointment",
                    questions = listOf(),
                    isCompleted = false
                )
            )
        )
    }

    Scaffold(
        topBar = {
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
            ExtendedFloatingActionButton(
                onClick = {
                    selectedAppointment = null // Ensure we're adding a new appointment
                    showAddAppointmentDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Appointment") }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (appointments.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No upcoming appointments",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tap the + button to add your first appointment",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appointments.value) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            onEdit = {
                                // Show edit dialog with the selected appointment
                                selectedAppointment = appointment
                                showAddAppointmentDialog = true
                            },
                            onDelete = {
                                // Show delete confirmation dialog
                                selectedAppointment = appointment
                                showDeleteConfirmDialog = true
                            },
                            onClick = {
                                // Show appointment details
                                selectedAppointment = appointment
                            }
                        )
                    }
                }
            }
        }
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
