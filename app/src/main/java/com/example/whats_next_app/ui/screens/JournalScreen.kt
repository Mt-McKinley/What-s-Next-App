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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// Material icons for UI actions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote

// Material Design 3 components
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavController

// Date handling
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen for managing journal entries about the cancer journey
 * Features:
 * - Record thoughts, feelings, symptoms, and experiences
 * - View past journal entries chronologically
 * - Edit and delete entries
 * - First-time user guidance
 *
 * @param navController Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(navController: NavController) {
    // Dialog visibility and entry selection states
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<JournalEntryData?>(null) }
    var showEntryDetailDialog by remember { mutableStateOf(false) }

    // State for storing the list of journal entries
    // Empty by default until user adds entries
    val journalEntries = remember {
        mutableStateOf(emptyList<JournalEntryData>())
    }

    // Track if first-time guidance has been shown using SharedPreferences
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("WhatsNextApp", 0) }
    val hasSeenGuidance = remember { prefs.getBoolean("has_seen_journal_guidance", false) }

    // Show guidance dialog for first-time users only when they have no entries
    var showGuidanceDialog by remember { mutableStateOf(journalEntries.value.isEmpty() && !hasSeenGuidance) }

    // Main scaffold with top app bar and floating action button
    Scaffold(
        topBar = {
            // App bar with back navigation
            TopAppBar(
                title = { Text("Journal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // Button to add a new journal entry
            ExtendedFloatingActionButton(
                onClick = {
                    // Clear selection to ensure we're adding a new entry
                    selectedEntry = null
                    showAddEntryDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("New Entry") },
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
            // Content displayed when there are no journal entries
            if (journalEntries.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Journal icon
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(72.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Title text
                    Text(
                        text = "Your Journal is Empty",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Instruction text
                    Text(
                        text = "Start documenting your journey by writing your first entry. Track your thoughts, feelings, and experiences.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Button to write the first entry
                    Button(
                        onClick = {
                            selectedEntry = null
                            showAddEntryDialog = true
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
                        Text("Write First Entry")
                    }
                }
            } else {
                // List of journal entries
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(journalEntries.value) { entry ->
                        // Individual journal entry card
                        JournalEntryCard(
                            entry = entry,
                            onClick = {
                                selectedEntry = entry
                                showEntryDetailDialog = true
                            },
                            onEdit = {
                                selectedEntry = entry
                                showAddEntryDialog = true
                            },
                            onDelete = {
                                selectedEntry = entry
                                showDeleteConfirmDialog = true
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
                prefs.edit().putBoolean("has_seen_journal_guidance", true).apply()
                showGuidanceDialog = false
            },
            title = { Text("Your Personal Journal") },
            text = {
                Column {
                    Text(
                        "Your journal is a private space to:",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("• Document your daily experiences")
                    Text("• Track your thoughts and feelings")
                    Text("• Record questions for your medical team")
                    Text("• Celebrate milestones and progress")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Start by writing your first journal entry using the '+' button.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Save that user has seen the guidance
                        prefs.edit().putBoolean("has_seen_journal_guidance", true).apply()
                        showGuidanceDialog = false
                        showAddEntryDialog = true
                    }
                ) {
                    Text("Write Entry")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Save that user has seen the guidance
                        prefs.edit().putBoolean("has_seen_journal_guidance", true).apply()
                        showGuidanceDialog = false
                    }
                ) {
                    Text("Got it")
                }
            }
        )
    }

    // Show journal entry details dialog
    if (showEntryDetailDialog && selectedEntry != null) {
        JournalEntryDetailDialog(
            entry = selectedEntry!!,
            onDismiss = {
                showEntryDetailDialog = false
                selectedEntry = null
            },
            onEdit = {
                showEntryDetailDialog = false
                showAddEntryDialog = true
            }
        )
    }

    // Show add/edit journal entry dialog
    if (showAddEntryDialog) {
        AddEditJournalEntryDialog(
            entry = selectedEntry,
            onDismiss = {
                showAddEntryDialog = false
                if (showEntryDetailDialog) {
                    selectedEntry = null
                }
            },
            onSave = { newEntry ->
                val updatedList = journalEntries.value.toMutableList()
                if (selectedEntry != null) {
                    // Update existing entry
                    val index = updatedList.indexOfFirst { it.id == selectedEntry!!.id }
                    if (index != -1) {
                        updatedList[index] = newEntry
                    }
                } else {
                    // Add new entry
                    updatedList.add(newEntry.copy(id = (journalEntries.value.maxOfOrNull { it.id } ?: 0) + 1))
                }
                journalEntries.value = updatedList
                showAddEntryDialog = false
                selectedEntry = null
            }
        )
    }

    // Show delete confirmation dialog
    if (showDeleteConfirmDialog && selectedEntry != null) {
        DeleteJournalEntryDialog(
            entry = selectedEntry!!,
            onDismiss = {
                showDeleteConfirmDialog = false
                selectedEntry = null
            },
            onConfirmDelete = {
                // Delete the entry from the list
                val updatedList = journalEntries.value.toMutableList()
                updatedList.remove(selectedEntry)
                journalEntries.value = updatedList

                showDeleteConfirmDialog = false
                selectedEntry = null
            }
        )
    }
}

// Data class representing a journal entry
data class JournalEntryData(
    val id: Int,
    val title: String,
    val date: Date,
    val content: String,
    val mood: String
)

// Composable function for displaying a single journal entry card
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryCard(
    entry: JournalEntryData,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Entry title
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleLarge
                )

                // Entry mood
                Text(
                    text = "Mood: ${entry.mood}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Entry date
            Text(
                text = dateFormat.format(entry.date),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Entry content preview
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
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

// Composable function for displaying journal entry details in a dialog
@Composable
fun JournalEntryDetailDialog(
    entry: JournalEntryData,
    onDismiss: () -> Unit,
    onEdit: () -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(entry.date)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(entry.title) },
        text = {
            Column {
                // Entry date
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Entry mood
                Text(
                    text = "Mood: ${entry.mood}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Entry content
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onEdit
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

// Composable function for adding or editing a journal entry
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditJournalEntryDialog(
    entry: JournalEntryData?,
    onDismiss: () -> Unit,
    onSave: (JournalEntryData) -> Unit
) {
    val isEditing = entry != null
    val title = remember { mutableStateOf(entry?.title ?: "") }
    val content = remember { mutableStateOf(entry?.content ?: "") }
    val mood = remember { mutableStateOf(entry?.mood ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edit Journal Entry" else "New Journal Entry") },
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
                    placeholder = { Text("e.g., First day of treatment") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mood field
                Text(
                    text = "Mood",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = mood.value,
                    onValueChange = { mood.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Hopeful, Anxious, Relieved") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Content field
                Text(
                    text = "Journal Entry",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                TextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Write about your day, feelings, or notes about treatment...") },
                    minLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newEntry = JournalEntryData(
                        id = entry?.id ?: 0,
                        title = title.value,
                        date = entry?.date ?: Date(), // Use current date for new entries
                        content = content.value,
                        mood = mood.value
                    )
                    onSave(newEntry)
                },
                enabled = title.value.isNotBlank() && content.value.isNotBlank()
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

// Composable function for confirming journal entry deletion
@Composable
fun DeleteJournalEntryDialog(
    entry: JournalEntryData,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete the journal entry \"${entry.title}\"? This action cannot be undone.") },
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
