package com.example.whats_next_app.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object JourneyGuide : Screen("journey_guide")
    object AppointmentTracker : Screen("appointments")
    object MedicationReminders : Screen("medications")
    object Journal : Screen("journal")
    object ResourceLibrary : Screen("resources")
    object EmergencyContacts : Screen("emergency_contacts")
    object Profile : Screen("profile")

    // Detail screens
    object AppointmentDetail : Screen("appointment/{appointmentId}") {
        fun createRoute(appointmentId: Int) = "appointment/$appointmentId"
    }

    object JournalDetail : Screen("journal/{entryId}") {
        fun createRoute(entryId: Int) = "journal/$entryId"
    }

    object StageDetail : Screen("stage/{stageId}") {
        fun createRoute(stageId: Int) = "stage/$stageId"
    }
}
