package com.ethiopianairlines.pilot.domain.model

import com.google.gson.annotations.SerializedName

data class Exam(
    @SerializedName("_id")
    val id: String = "",
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val durationMinutes: Int,
    val passingScore: Int,
    val totalQuestions: Int,
    val isActive: Boolean,
    val questions: List<Question> = emptyList(),
    val createdAt: String = "",
    val updatedAt: String = ""
)

//data class Question(
//    val id: String = "",
//    val text: String,
//    val options: List<String>,
//    val correctOptionIndex: Int,
//    val explanation: String
//)

enum class ExamCategory {
    PILOT_TRAINEE,
    PILOT_INSTRUCTOR,
    PILOT_EXAMINER
}

enum class ExamDifficulty {
    EASY,
    MEDIUM,
    HARD
} 