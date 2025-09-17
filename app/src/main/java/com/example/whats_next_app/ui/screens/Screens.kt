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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whats_next_app.model.ResourceLink
import com.example.whats_next_app.model.ResourceType
import com.example.whats_next_app.navigation.Screen

@Composable
fun JourneyGuideScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val journeyStages = listOf(
        "Diagnosis",
        "Treatment Planning",
        "Active Treatment",
        "Post-Treatment",
        "Survivorship"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        text = "Cancer Journey Guide",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Based on Dr. Lisa Baker's 'What's Next'",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            TabRow(selectedTabIndex = selectedTabIndex) {
                journeyStages.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> DiagnosisStageContent(navController)
                1 -> TreatmentPlanningStageContent(navController)
                2 -> ActiveTreatmentStageContent(navController)
                3 -> PostTreatmentStageContent(navController)
                4 -> SurvivorshipStageContent(navController)
            }
        }
    }
}

@Composable
fun DiagnosisStageContent(navController: NavController) {
    val checklistItems = listOf(
        "Schedule appointments with specialists",
        "Gather medical records",
        "Ask about diagnostic tests",
        "Understand your diagnosis",
        "Bring a support person to appointments"
    )

    val resources = listOf(
        ResourceLink(
            "Understanding Your Diagnosis",
            "https://example.com/diagnosis",
            ResourceType.ARTICLE
        ),
        ResourceLink(
            "Questions to Ask Your Doctor",
            "https://example.com/questions",
            ResourceType.CHECKLIST
        ),
        ResourceLink(
            "Finding Support After Diagnosis",
            "https://example.com/support",
            ResourceType.SUPPORT_GROUP
        )
    )

    JourneyStageContent(
        title = "Diagnosis Stage",
        description = "The diagnosis stage can be overwhelming. This is a time to gather information, understand your specific cancer diagnosis, and prepare for the journey ahead.",
        checklistItems = checklistItems,
        resources = resources,
        navController = navController
    )
}

@Composable
fun TreatmentPlanningStageContent(navController: NavController) {
    val checklistItems = listOf(
        "Understand all treatment options",
        "Consider getting a second opinion",
        "Ask about clinical trials",
        "Understand potential side effects",
        "Create a support plan"
    )

    val resources = listOf(
        ResourceLink(
            "Treatment Options Explained",
            "https://example.com/treatments",
            ResourceType.ARTICLE
        ),
        ResourceLink(
            "Finding Clinical Trials",
            "https://example.com/trials",
            ResourceType.MEDICAL_RESOURCE
        ),
        ResourceLink(
            "Creating Your Support Network",
            "https://example.com/network",
            ResourceType.VIDEO
        )
    )

    JourneyStageContent(
        title = "Treatment Planning Stage",
        description = "During treatment planning, you'll work with your medical team to decide on the best approach for your specific cancer. Take time to understand all options.",
        checklistItems = checklistItems,
        resources = resources,
        navController = navController
    )
}

@Composable
fun ActiveTreatmentStageContent(navController: NavController) {
    val checklistItems = listOf(
        "Prepare for treatment side effects",
        "Set up a medication schedule",
        "Track symptoms and questions",
        "Arrange transportation to appointments",
        "Consider work accommodations"
    )

    val resources = listOf(
        ResourceLink(
            "Managing Treatment Side Effects",
            "https://example.com/side-effects",
            ResourceType.ARTICLE
        ),
        ResourceLink(
            "Nutrition During Treatment",
            "https://example.com/nutrition",
            ResourceType.VIDEO
        ),
        ResourceLink(
            "Financial Resources",
            "https://example.com/financial",
            ResourceType.MEDICAL_RESOURCE
        )
    )

    JourneyStageContent(
        title = "Active Treatment Stage",
        description = "During active treatment, focus on self-care, managing side effects, and maintaining your support network. Keep track of your symptoms and questions for your medical team.",
        checklistItems = checklistItems,
        resources = resources,
        navController = navController
    )
}

@Composable
fun PostTreatmentStageContent(navController: NavController) {
    val checklistItems = listOf(
        "Understand your follow-up care plan",
        "Know signs of recurrence to watch for",
        "Schedule regular check-ups",
        "Address lingering side effects",
        "Focus on emotional recovery"
    )

    val resources = listOf(
        ResourceLink(
            "Post-Treatment Care Plans",
            "https://example.com/post-treatment",
            ResourceType.ARTICLE
        ),
        ResourceLink(
            "Recognizing Signs of Recurrence",
            "https://example.com/recurrence",
            ResourceType.CHECKLIST
        ),
        ResourceLink(
            "Emotional Healing After Cancer",
            "https://example.com/emotional",
            ResourceType.SUPPORT_GROUP
        )
    )

    JourneyStageContent(
        title = "Post-Treatment Stage",
        description = "After completing treatment, you'll transition to follow-up care. This period may bring mixed emotions as you adjust to life after active treatment.",
        checklistItems = checklistItems,
        resources = resources,
        navController = navController
    )
}

@Composable
fun SurvivorshipStageContent(navController: NavController) {
    val checklistItems = listOf(
        "Maintain follow-up appointments",
        "Adopt healthy lifestyle habits",
        "Address long-term effects",
        "Connect with survivor groups",
        "Find your 'new normal'"
    )

    val resources = listOf(
        ResourceLink(
            "Life After Cancer",
            "https://example.com/survivorship",
            ResourceType.ARTICLE
        ),
        ResourceLink(
            "Healthy Living for Survivors",
            "https://example.com/healthy-living",
            ResourceType.VIDEO
        ),
        ResourceLink(
            "Cancer Survivor Communities",
            "https://example.com/communities",
            ResourceType.SUPPORT_GROUP
        )
    )

    JourneyStageContent(
        title = "Survivorship Stage",
        description = "Survivorship is about embracing life after cancer while managing the long-term effects of your experience. Focus on wellness and finding meaning in your journey.",
        checklistItems = checklistItems,
        resources = resources,
        navController = navController
    )
}

@Composable
fun JourneyStageContent(
    title: String,
    description: String,
    checklistItems: List<String>,
    resources: List<ResourceLink>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Checklist",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Text(
                            text = "Recommended Actions",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    checklistItems.forEach { item ->
                        Text(
                            text = "• $item",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Helpful Resources",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(resources) { resource ->
            ResourceCard(resource = resource)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate(Screen.ResourceLibrary.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Resources")
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View All",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceCard(resource: ResourceLink) {
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
            val icon = when (resource.type) {
                ResourceType.ARTICLE -> Icons.Default.Info
                ResourceType.VIDEO -> Icons.Default.Book
                ResourceType.MEDICAL_RESOURCE -> Icons.Default.Info
                ResourceType.SUPPORT_GROUP -> Icons.Default.Info
                ResourceType.CHECKLIST -> Icons.Default.CheckCircle
            }

            Icon(
                imageVector = icon,
                contentDescription = resource.type.name,
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
