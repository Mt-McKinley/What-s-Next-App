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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// Material icons for UI actions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Medication

// Material Design 3 components
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material3.TextButton

// State management and composition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// UI layout and configuration
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavController

/**
 * Screen for managing medication reminders
 * Features:
 * - Track and manage all medications
 * - Set up dosage information and schedules
 * - Mark medications as taken
 * - Add, edit, and remove medications
 *
 * @param navController Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationRemindersScreen(navController: NavController) {
    // Dialog state for adding/editing medications
    var showAddMedicationDialog by remember { mutableStateOf(false) }

    // Currently selected medication for editing
    var selectedMedication by remember { mutableStateOf<String?>(null) }

    // State for storing medication list
    // Empty by default until user adds medications
    val medications = remember {
        mutableStateOf(
            emptyList<String>()
        )
    }

    // Main scaffold with top app bar and floating action button
    Scaffold(
        topBar = {
            // App bar with back navigation
            TopAppBar(
                title = { Text("Medication Reminders") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // Button to add a new medication
            ExtendedFloatingActionButton(
                onClick = {
                    selectedMedication = null // Ensure we're adding a new medication
                    showAddMedicationDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Medication") },
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
            // Show empty state when no medications exist
            if (medications.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Empty state message
                    Text(
                        text = "No medications scheduled",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instruction for adding first medication
                    Text(
                        text = "Tap the + button to add your medications",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // List of medications
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Today's medications section
                    item {
                        Text(
                            text = "Today's Medications",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // List of current medications
                    items(medications.value) { medication ->
                        MedicationCard(
                            medication = medication,
                            onEdit = {
                                selectedMedication = medication
                                showAddMedicationDialog = true
                            },
                            onDelete = {
                                // Create a new list without the deleted medication
                                val updatedList = medications.value.toMutableList()
                                updatedList.remove(medication)
                                medications.value = updatedList
                            }
                        )
                    }

                    // Upcoming medications section (currently empty)
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Upcoming Medications",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "No upcoming medications scheduled",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }

    // Show add/edit medication dialog when requested
    if (showAddMedicationDialog) {
        AddMedicationDialog(
            medication = selectedMedication,
            onDismiss = { showAddMedicationDialog = false },
            onSave = { newMedication ->
                if (selectedMedication != null) {
                    // Editing existing medication
                    val updatedList = medications.value.toMutableList()
                    val index = updatedList.indexOf(selectedMedication)
                    if (index != -1) {
                        updatedList[index] = newMedication
                        medications.value = updatedList
                    }
                } else {
                    // Adding new medication
                    val updatedList = medications.value.toMutableList()
                    updatedList.add(newMedication)
                    medications.value = updatedList
                }
                showAddMedicationDialog = false
                selectedMedication = null
            }
        )
    }
}

/**
 * Card representing a single medication with checkbox for completion
 *
 * @param medication The medication string in format "Name - Dosage - Schedule"
 * @param onEdit Callback when edit button is clicked
 * @param onDelete Callback when delete button is clicked
 */
@Composable
fun MedicationCard(
    medication: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // State to track whether medication has been taken
    var isCompleted by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Medication info with checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox to mark as taken
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Medication details
                Column {
                    Text(
                        text = medication,
                        style = MaterialTheme.typography.titleMedium,
                        // Gray out text when completed
                        color = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Action buttons
            Row {
                // Edit button
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Delete button
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Dialog for adding or editing medication information
 *
 * @param medication Existing medication string if editing, null if adding new
 * @param onDismiss Callback when dialog is dismissed
 * @param onSave Callback when medication is saved with the new medication string
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationDialog(
    medication: String?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val isEditing = medication != null

    // Split the medication string into its components if we're editing
    val parts = medication?.split(" - ") ?: listOf("", "", "")

    // State for medication information fields
    var medicationName by remember { mutableStateOf(if (parts.size > 0) parts[0] else "") }
    var dosage by remember { mutableStateOf(if (parts.size > 1) parts[1] else "") }
    var schedule by remember { mutableStateOf(if (parts.size > 2) parts[2] else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edit Medication" else "Add New Medication") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Medication name field
                Text(
                    text = "Medication Name",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                androidx.compose.material3.TextField(
                    value = medicationName,
                    onValueChange = { medicationName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Paclitaxel") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dosage field
                Text(
                    text = "Dosage",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                androidx.compose.material3.TextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., 1 tablet") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Schedule field
                Text(
                    text = "Schedule",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                androidx.compose.material3.TextField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Every morning with food") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            // Save button
            TextButton(
                onClick = {
                    // Combine fields into a single medication string
                    val newMedication = "$medicationName - $dosage - $schedule"
                    onSave(newMedication)
                },
                enabled = medicationName.isNotBlank() && dosage.isNotBlank() && schedule.isNotBlank()
            ) {
                Text(if (isEditing) "Update" else "Save")
            }
        },
        dismissButton = {
            // Cancel button
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
