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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person

// Material Design 3 components
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

// State management and composition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// UI layout and configuration
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavController

// App-specific models
import com.example.whats_next_app.model.Contact

/**
 * Screen for managing emergency contacts
 * Features:
 * - Quick access emergency services call button
 * - List of emergency and non-emergency contacts
 * - Add, view, and manage important contacts
 *
 * @param navController Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(navController: NavController) {
    // State for storing contacts
    // Empty by default until user adds contacts
    val contacts = remember {
        emptyList<Contact>()
    }

    // Main scaffold with top app bar and floating action button
    Scaffold(
        topBar = {
            // App bar with back navigation
            TopAppBar(
                title = { Text("Emergency Contacts") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // Button to add a new contact
            ExtendedFloatingActionButton(
                onClick = { /* Add contact functionality */ },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Contact") }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Emergency services call button (911)
                EmergencyCallButton()

                Spacer(modifier = Modifier.height(24.dp))

                // Show empty state when no contacts exist
                if (contacts.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Empty state message
                        Text(
                            text = "No emergency contacts added",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Instruction text
                        Text(
                            text = "Add important contacts for quick access during emergencies",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Section title for emergency contacts
                    Text(
                        text = "Emergency Contacts",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // List of contacts separated by emergency and non-emergency
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Emergency contacts section
                        val emergencyContacts = contacts.filter { it.isEmergencyContact }
                        items(emergencyContacts) { contact ->
                            ContactCard(contact = contact)
                        }

                        // Other contacts section header
                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Other Contacts",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Non-emergency contacts
                        val otherContacts = contacts.filter { !it.isEmergencyContact }
                        items(otherContacts) { contact ->
                            ContactCard(contact = contact)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Emergency call button for directly calling 911
 * Highlighted with error colors to indicate importance and urgency
 */
@Composable
fun EmergencyCallButton() {
    Button(
        onClick = { /* Call emergency services */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Phone icon
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Emergency Call"
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Emergency number text
            Text(
                text = "Call 911 (Emergency Services)",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Card displaying a single contact with their information
 *
 * @param contact The contact to display
 */
@Composable
fun ContactCard(contact: Contact) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contact icon
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Contact details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Contact name
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium
                )

                // Relationship to contact
                Text(
                    text = contact.relationship,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                // Phone number
                Text(
                    text = contact.phone,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Call button
            IconButton(
                onClick = { /* Call contact */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
