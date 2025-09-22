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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// Material icons for UI actions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VideoLibrary

// Material Design 3 components
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Navigation
import androidx.navigation.NavController

// App-specific models
import com.example.whats_next_app.model.ResourceLink
import com.example.whats_next_app.model.ResourceType

/**
 * Screen for accessing educational resources and support groups
 * Features:
 * - Categorized resources (articles, videos, support groups)
 * - Filtering by resource type
 * - Resources from Dr. Lisa Baker's "What's Next" book
 *
 * @param navController Navigation controller for screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceLibraryScreen(navController: NavController) {
    // State for tracking the selected tab
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Tab categories for filtering resources
    val tabTitles = listOf("All Resources", "Articles", "Videos", "Support Groups")

    // State for storing resource links
    // Empty by default until resources are added
    val resources = remember {
        emptyList<ResourceLink>()
    }

    // Main scaffold with top app bar
    Scaffold(
        topBar = {
            // App bar with back navigation
            TopAppBar(
                title = { Text("Resource Library") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            Column {
                // Tab row for filtering resources by type
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                // Filter resources based on selected tab
                val filteredResources = when (selectedTabIndex) {
                    0 -> resources // All resources
                    1 -> resources.filter { it.type == ResourceType.ARTICLE }
                    2 -> resources.filter { it.type == ResourceType.VIDEO }
                    3 -> resources.filter { it.type == ResourceType.SUPPORT_GROUP }
                    else -> resources
                }

                // Display empty state if no resources match the filter
                if (filteredResources.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Empty state message
                        Text(
                            text = "No resources found",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {
                    // List of filtered resources
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Source attribution
                        item {
                            Text(
                                text = "From Dr. Lisa Baker's \"What's Next\"",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // Display each resource as a card
                        items(filteredResources) { resource ->
                            ResourceItemCard(resource = resource)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card displaying a single resource with appropriate icon based on type
 *
 * @param resource The resource link to display
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceItemCard(resource: ResourceLink) {
    // Select appropriate icon based on resource type
    val (icon, contentDescription) = when (resource.type) {
        ResourceType.ARTICLE -> Icons.Default.Book to "Article"
        ResourceType.VIDEO -> Icons.Default.VideoLibrary to "Video"
        ResourceType.SUPPORT_GROUP -> Icons.Default.Info to "Support Group"
        else -> Icons.Default.Info to "Resource"
    }

    // Clickable card to open the resource
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* Open resource link */ },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Resource type icon
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary
            )

            // Resource details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // Resource title
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleMedium
                )

                // Resource type
                Text(
                    text = resource.type.name.replace('_', ' ').lowercase().capitalize(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Forward arrow indicating clickable
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
