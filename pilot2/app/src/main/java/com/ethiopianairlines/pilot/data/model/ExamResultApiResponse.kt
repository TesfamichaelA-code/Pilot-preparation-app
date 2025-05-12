package com.ethiopianairlines.pilot.data.model

import com.google.gson.annotations.SerializedName

data class ExamResultApiResponse(
    val status: String,
    val data: ExamResultDto,
    val timestamp: String
)

data class ExamResultListApiResponse(
    val status: String,
    val data: List<ExamResultDto>,
    val timestamp: String
)

data class ExamResultDto(
    @SerializedName("_id")
    val id: String,
    val examId: String,
    val examTitle: String,
    val userId: String,
    val score: Int,
    val totalQuestions: Int,
    val answers: Map<String, Int>,
    val completedAt: String,
    val isPassed: Boolean
) 