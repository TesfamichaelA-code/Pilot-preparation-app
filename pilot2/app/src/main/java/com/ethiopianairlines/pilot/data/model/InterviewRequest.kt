package com.ethiopianairlines.pilot.data.model

data class InterviewRequest(
    val question: String,
    val sampleAnswer: String,
    val category: String,
    val difficulty: String,
    val tipsForAnswering: String,
    val yearAsked: Int
) 