package com.example.whats_next_app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whats_next_app.ui.screens.AppointmentTrackerScreen
import com.example.whats_next_app.ui.screens.EmergencyContactsScreen
import com.example.whats_next_app.ui.screens.HomeScreen
import com.example.whats_next_app.ui.screens.JournalScreen
import com.example.whats_next_app.ui.screens.JourneyGuideScreen
import com.example.whats_next_app.ui.screens.MedicationRemindersScreen
import com.example.whats_next_app.ui.screens.ProfileScreen
import com.example.whats_next_app.ui.screens.ResourceLibraryScreen

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun WhatsNextAppNavigation() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Journey", Screen.JourneyGuide.route, Icons.Default.Route),
        BottomNavItem("Appointments", Screen.AppointmentTracker.route, Icons.Default.CalendarMonth),
        BottomNavItem("Medications", Screen.MedicationReminders.route, Icons.Default.Medication),
        BottomNavItem("Journal", Screen.Journal.route, Icons.Default.EditNote)
    )

    Scaffold(
        bottomBar = { WhatsNextBottomNavigation(navController, bottomNavItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.JourneyGuide.route) {
                JourneyGuideScreen(navController)
            }
            composable(Screen.AppointmentTracker.route) {
                AppointmentTrackerScreen(navController)
            }
            composable(Screen.MedicationReminders.route) {
                MedicationRemindersScreen(navController)
            }
            composable(Screen.Journal.route) {
                JournalScreen(navController)
            }
            composable(Screen.ResourceLibrary.route) {
                ResourceLibraryScreen(navController)
            }
            composable(Screen.EmergencyContacts.route) {
                EmergencyContactsScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }

            // Detail screens with parameters
            composable(
                route = Screen.AppointmentDetail.route
            ) {
                // AppointmentDetailScreen()
            }

            composable(
                route = Screen.JournalDetail.route
            ) {
                // JournalDetailScreen()
            }

            composable(
                route = Screen.StageDetail.route
            ) {
                // StageDetailScreen()
            }
        }
    }
}

@Composable
fun WhatsNextBottomNavigation(navController: NavHostController, items: List<BottomNavItem>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.name) },
                label = { Text(item.name, style = MaterialTheme.typography.bodySmall) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
