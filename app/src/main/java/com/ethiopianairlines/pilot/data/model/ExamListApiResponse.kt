package com.ethiopianairlines.pilot.data.model // Or a sub-package like data.remote.dto

// This new class will wrap the list of exams
data class ExamListApiResponse(
    val data: List<com.ethiopianairlines.pilot.data.model.Exam>, // Uses the corrected DTO above
    // If your API also includes "status" and "timestamp" at this root level, add them here:
     val status: String?,
    // val timestamp: String?
)