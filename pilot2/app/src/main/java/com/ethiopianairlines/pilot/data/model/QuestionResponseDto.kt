package com.ethiopianairlines.pilot.data.model

import com.google.gson.annotations.SerializedName

data class QuestionResponseDto(
    @SerializedName("_id")
    val id: String? = null,
    val examId: String? = null,
    val text: String? = null,
    val options: List<String>? = null,
    val correctAnswer: Int = 0, // Index from backend
    val explanation: String? = null,
//    val createdAt: String,
//    val updatedAt: String,
//    @SerializedName("__v")
//    val version: Int? = null
) {
    // Optional: a toDomain function if you don't use a separate mapper
    fun toDomainModel(): com.ethiopianairlines.pilot.domain.model.Question {
        return com.ethiopianairlines.pilot.domain.model.Question(
            id = this.id ?: "",
            examId = this.examId ?: "",
            text = this.text ?: "",
            options = this.options ?: emptyList(),
            correctAnswerIndex = this.correctAnswer,
            explanation = this.explanation ?: ""
//            createdAt = this.createdAt,
//            updatedAt = this.updatedAt
        )
    }
} 