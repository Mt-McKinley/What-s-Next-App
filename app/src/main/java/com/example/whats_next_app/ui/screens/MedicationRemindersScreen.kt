package com.example.whats_next_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Medication
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationRemindersScreen(navController: NavController) {
    var showAddMedicationDialog by remember { mutableStateOf(false) }
    var selectedMedication by remember { mutableStateOf<String?>(null) }

    val medications = remember {
        mutableStateOf(
            listOf(
                "Paclitaxel - 1 tablet - 9:00 AM",
                "Dexamethasone - 2 tablets - 8:00 PM",
                "Ondansetron - 1 tablet - As needed for nausea",
                "Lorazepam - 1 tablet - Before chemotherapy"
            )
        )
    }

    Scaffold(
        topBar = {
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
            ExtendedFloatingActionButton(
                onClick = {
                    selectedMedication = null // Ensure we're adding a new medication
                    showAddMedicationDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Medication") }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (medications.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No medications scheduled",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tap the + button to add your medications",
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
                    item {
                        Text(
                            text = "Today's Medications",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

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

    // Show add/edit medication dialog
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

@Composable
fun MedicationCard(
    medication: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = medication,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                    )
                }
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
    }
}

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
                    placeholder = { Text("e.g., 9:00 AM or As needed") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = {
                    // Combine the fields into a medication string
                    val newMedication = "$medicationName - $dosage - $schedule"
                    onSave(newMedication)
                },
                enabled = medicationName.isNotBlank() && dosage.isNotBlank() && schedule.isNotBlank()
            ) {
                Text(if (isEditing) "Update" else "Save")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
