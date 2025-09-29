package com.example.whats_next_app.ui.screens

// Layout imports for UI composition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image

// Material icon imports for UI elements
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Book

// Material design component imports
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField

// State management imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// UI configuration imports
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri

// Navigation imports
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// App-specific imports
import com.example.whats_next_app.navigation.Screen
import com.example.whats_next_app.ui.theme.WhatsNextAppTheme
import com.example.whats_next_app.ui.theme.MintGreen
import com.example.whats_next_app.ui.theme.SalmonPink
import com.example.whats_next_app.ui.theme.LightGray

// Import for R class to resolve resource references
import com.example.whats_next_app.R

// Date formatting imports
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data structure for category cards
data class CategoryCard(
    val title: String,
    val icon: ImageVector,
    val route: String
)

/**
 * Color palette for the flowchart design
 * Based on the pink, yellow, and orange theme from the provided flowchart
 */
private val FlowchartPink = Color(0xFFE91E63)       // Primary pink color
private val FlowchartYellow = Color(0xFFFFD700)     // Yellow accent color
private val FlowchartOrange = Color(0xFFFFB347)     // Orange accent color
private val FlowchartBlue = Color(0xFF90CAF9)       // Light blue accent color
private val FlowchartLightSalmon = Color(0xFFFFA07A) // Light salmon accent color
private val FlowchartCreamBackground = Color(0xFFFFF8E1) // Light cream background
private val FlowchartCompletedGreen = Color(0xFF8BC34A) // Completed stage green
private val FlowchartErrorRed = Color(0xFFF44336)   // Error/emergency red

/**
 * Main home screen of the What's Next app.
 * This is the central hub that displays all main features of the app including:
 * - "What's Next" app header with origami logo
 * - Search bar for finding information
 * - Category cards arranged in a grid layout for easy access
 * - Help button in bottom corner
 *
 * @param navController Navigation controller to handle screen transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // State for search field
    var searchText by remember { mutableStateOf("") }

    // Define category cards data
    val categories = listOf(
        CategoryCard("Medical Staff", Icons.Default.Info, Screen.ResourceLibrary.route),
        CategoryCard("Care Hacks", Icons.Default.Help, Screen.ResourceLibrary.route),
        CategoryCard("First Steps", Icons.Default.Route, Screen.ResourceLibrary.route),
        CategoryCard("Appointments", Icons.Default.CalendarMonth, Screen.AppointmentTracker.route),
        CategoryCard("Medications", Icons.Default.Medication, Screen.MedicationReminders.route),
        CategoryCard("Journal", Icons.Default.Info, Screen.Journal.route),
        CategoryCard("Emergency", Icons.Default.Info, Screen.EmergencyContacts.route),
        CategoryCard("Resources", Icons.Default.Help, Screen.ResourceLibrary.route),
        CategoryCard("My Journey", Icons.Default.Route, Screen.JourneyGuide.route)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Show help dialog */ },
                containerColor = MintGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.White // White background as in the image
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App header with mint green title and origami bird logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "What's Next?",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Normal,
                            color = MintGreen,
                            fontSize = 36.sp
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    // Origami bird logo
                    Spacer(modifier = Modifier.width(8.dp))
                    OrigamiBirdLogo(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Search bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = { Text("Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LightGray,
                        unfocusedContainerColor = LightGray,
                        disabledContainerColor = LightGray,
                        focusedIndicatorColor = MintGreen,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Category grid cards
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        CategoryItemCard(
                            title = category.title,
                            icon = category.icon,
                            onClick = { navController.navigate(category.route) }
                        )
                    }
                }

                // Book link widget for the "What's Next" book
                BookLinkWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

/**
 * A card representing a category item in the home screen grid
 * Styled to match the salmon/peach colored cards in the image
 *
 * @param title The category title
 * @param icon The icon for this category
 * @param onClick Callback when this category is clicked
 */
@Composable
fun CategoryItemCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        // Salmon colored image placeholder
        Card(
            modifier = Modifier
                .size(width = 90.dp, height = 70.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = SalmonPink
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Category title below the image
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = SalmonPink,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * Dialog that allows users to choose which type of item to add or view
 *
 * @param title Dialog title
 * @param message Dialog message or description
 * @param onDismiss Callback when dialog is dismissed
 * @param onAppointment Callback when appointment option is selected
 * @param onMedication Callback when medication option is selected
 * @param onJournal Callback when journal option is selected
 * @param showOptions Whether to show all options (true) or just OK button (false)
 */
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
                // Show all action options if no specific action was selected
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
                // Just show OK button if viewing a specific item
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

/**
 * Styled header card based on the flowchart design
 * Uses the pink color block style from the flowchart
 */
@Composable
fun FlowchartHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = FlowchartPink // Pink from flowchart
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "What's Next",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val date = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Cancer Journey Companion",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White
                )
            )
        }
    }
}

/**
 * Styled card for each journey stage in the cancer journey flowchart
 *
 * @param stage The name of the journey stage
 * @param isCompleted Whether this stage has been completed
 * @param isCurrentStage Whether this is the current active stage
 * @param onClick Callback when the stage card is clicked
 */
@Composable
fun JourneyStageCard(
    stage: String,
    isCompleted: Boolean = false,
    isCurrentStage: Boolean = false,
    onClick: () -> Unit
) {
    // Define colors based on stage status
    val backgroundColor = when {
        isCurrentStage -> FlowchartPink // Current stage is highlighted in pink
        isCompleted -> FlowchartCompletedGreen // Completed is green
        else -> Color.LightGray // Future stages are gray
    }

    val textColor = Color.White

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status indicator
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (isCompleted) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = textColor,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = backgroundColor,
                            modifier = Modifier.size(24.dp)
                        )
                    } else if (isCurrentStage) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Stage name
                Text(
                    text = stage,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = textColor,
                        fontWeight = if (isCurrentStage) FontWeight.Bold else FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                // Arrow indicator
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View details",
                    tint = textColor
                )
            }
        }

        // Add connector line between cards to show the flow
        if (!isCompleted || isCurrentStage) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(2.dp)
                    .background(backgroundColor)
            )
        }
    }
}

/**
 * Styled card for the side path options in the flowchart
 *
 * @param title The name of the side path option
 * @param icon The icon to display for this option
 * @param backgroundColor The background color for this option's block
 * @param onClick Callback when the option is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidePathCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = backgroundColor
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "View details",
                tint = Color.White
            )
        }
    }
}

/**
 * Styled emergency contact button based on the flowchart design
 * Uses the red color block for emphasis
 *
 * @param onClick Callback when the emergency button is clicked
 */
@Composable
fun FlowchartEmergencyContactButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = FlowchartErrorRed // Red for emergency
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Emergency",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Emergency Contacts",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

/**
 * Card that displays an inspirational quote of the day
 * Encourages and motivates users in their cancer journey
 * Styled to match flowchart design
 *
 * @param quote The quote text to display
 * @param author The author of the quote
 */
@Composable
fun DailyQuoteCard(quote: String, author: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = FlowchartLightSalmon.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "— $author",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * A custom origami bird logo using the PNG image resource
 *
 * @param modifier Modifier to be applied to the Image
 */
@Composable
fun OrigamiBirdLogo(modifier: Modifier = Modifier) {
    // Create a custom painter to directly load the PNG image resource
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier("origami_bird", "drawable", context.packageName)

    Image(
        painter = painterResource(id = resourceId),
        contentDescription = "Origami Bird Logo",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

/**
 * A widget that displays a link to the "What's Next" book on Amazon
 * When clicked, it opens the Amazon link in the user's web browser
 *
 * @param modifier Modifier to be applied to the widget
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookLinkWidget(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val amazonBookUrl = "https://www.amazon.com/Whats-Next-Navigating-Extended-Hospital/dp/196267438X/"

    ElevatedCard(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(amazonBookUrl))
            context.startActivity(intent)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = FlowchartBlue.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = "Book",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "What's Next?: Navigating Your Child's Extended Hospital Journey",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "By Dr. Lisa Baker",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Get the book on Amazon",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open Link",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
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
