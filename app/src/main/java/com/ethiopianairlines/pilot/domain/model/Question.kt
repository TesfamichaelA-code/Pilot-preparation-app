package com.ethiopianairlines.pilot.domain.model

import com.google.gson.annotations.SerializedName


data class Question(
    @SerializedName("_id")
    val id: String = "", // Will be set by backend
    val examId: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int, // Renamed from correctAnswer for clarity
    val explanation: String?, // Nullable if optional
    val createdAt: String = "",
    val updatedAt: String = ""
)