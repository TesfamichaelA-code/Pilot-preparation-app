package com.ethiopianairlines.pilot.data.model

data class AddQuestionApiResponse(
    val data: QuestionResponseDto, // The actual question data is nested here
    val status: String,
    val timestamp: String
)