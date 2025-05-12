package com.ethiopianairlines.pilot.data.model

data class QuestionListApiResponse(
    val data: List<QuestionResponseDto>, // The actual list of questions is nested here
    val status: String,
    val timestamp: String
    // Add any other top-level fields from the response if they exist
)