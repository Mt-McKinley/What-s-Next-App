package com.example.whats_next_app.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VideoLibrary
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whats_next_app.model.ResourceLink
import com.example.whats_next_app.model.ResourceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceLibraryScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabTitles = listOf("All Resources", "Articles", "Videos", "Support Groups")

    val resources = remember {
        listOf(
            ResourceLink(
                "Understanding Your Diagnosis",
                "https://example.com/diagnosis",
                ResourceType.ARTICLE
            ),
            ResourceLink(
                "What to Expect During Chemotherapy",
                "https://example.com/chemotherapy",
                ResourceType.VIDEO
            ),
            ResourceLink(
                "Radiation Therapy Explained",
                "https://example.com/radiation",
                ResourceType.ARTICLE
            ),
            ResourceLink(
                "Finding Your Support Network",
                "https://example.com/support",
                ResourceType.SUPPORT_GROUP
            ),
            ResourceLink(
                "Nutrition During Treatment",
                "https://example.com/nutrition",
                ResourceType.ARTICLE
            ),
            ResourceLink(
                "Managing Treatment Side Effects",
                "https://example.com/side-effects",
                ResourceType.VIDEO
            ),
            ResourceLink(
                "Dr. Lisa Baker's Approach to Cancer Care",
                "https://example.com/dr-baker",
                ResourceType.VIDEO
            ),
            ResourceLink(
                "Patient Stories: Journey to Recovery",
                "https://example.com/stories",
                ResourceType.SUPPORT_GROUP
            )
        )
    }

    Scaffold(
        topBar = {
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
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                val filteredResources = when (selectedTabIndex) {
                    0 -> resources
                    1 -> resources.filter { it.type == ResourceType.ARTICLE }
                    2 -> resources.filter { it.type == ResourceType.VIDEO }
                    3 -> resources.filter { it.type == ResourceType.SUPPORT_GROUP }
                    else -> resources
                }

                if (filteredResources.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No resources found",
                            style = MaterialTheme.typography.titleLarge
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
                                text = "From Dr. Lisa Baker's \"What's Next\"",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(filteredResources) { resource ->
                            ResourceItemCard(resource = resource)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceItemCard(resource: ResourceLink) {
    val (icon, contentDescription) = when (resource.type) {
        ResourceType.ARTICLE -> Icons.Default.Book to "Article"
        ResourceType.VIDEO -> Icons.Default.VideoLibrary to "Video"
        ResourceType.SUPPORT_GROUP -> Icons.Default.Info to "Support Group"
        else -> Icons.Default.Info to "Resource"
    }

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
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = resource.type.name.replace('_', ' ').lowercase().capitalize(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
