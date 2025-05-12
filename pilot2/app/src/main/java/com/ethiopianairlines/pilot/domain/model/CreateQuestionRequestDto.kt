package com.ethiopianairlines.pilot.data.model

data class CreateQuestionRequestDto(
    val text: String,
    val options: List<String>,
    val correctAnswer: Int, // This is the index, matches backend
    val explanation: String?
)