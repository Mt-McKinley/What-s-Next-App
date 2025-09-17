package com.example.whats_next_app.model

data class CancerJourneyStage(
    val id: Int,
    val title: String,
    val description: String,
    val suggestedActions: List<String>,
    val resourceLinks: List<ResourceLink>
)

data class ResourceLink(
    val title: String,
    val url: String,
    val type: ResourceType
)

enum class ResourceType {
    ARTICLE,
    VIDEO,
    MEDICAL_RESOURCE,
    SUPPORT_GROUP,
    CHECKLIST
}

data class Appointment(
    val id: Int,
    val title: String,
    val doctorName: String,
    val dateTime: Long,
    val location: String,
    val notes: String,
    val questions: List<String>,
    val isCompleted: Boolean = false
)

data class MedicationReminder(
    val id: Int,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val startDate: Long,
    val endDate: Long?,
    val timeOfDay: List<Long>,
    val notes: String
)

data class JournalEntry(
    val id: Int,
    val date: Long,
    val title: String,
    val content: String,
    val mood: Mood,
    val symptoms: List<Symptom>
)

enum class Mood {
    VERY_GOOD,
    GOOD,
    NEUTRAL,
    BAD,
    VERY_BAD
}

data class Symptom(
    val name: String,
    val severity: Int, // 1-10
    val notes: String
)

data class UserProfile(
    val name: String,
    val cancerType: String,
    val diagnosisDate: Long,
    val currentStage: Int,
    val emergencyContacts: List<Contact>
)

data class Contact(
    val name: String,
    val phone: String,
    val relationship: String,
    val isEmergencyContact: Boolean
)
