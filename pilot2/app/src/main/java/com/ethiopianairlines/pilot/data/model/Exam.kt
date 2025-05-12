package com.ethiopianairlines.pilot.data.model

import com.google.gson.annotations.SerializedName // Or Moshi's @Json

// Let's rename this to avoid confusion with domain.model.Exam
// Or ensure it's always referred to with its full package if you keep the name Exam.
// For clarity in this example, I'll call it ExamNetworkDto.
// If you keep it as 'Exam', ensure all usages are qualified.
// For this fix, I'll assume you want to keep 'Exam' and will refer to it as data.model.Exam
data class Exam( // This is com.ethiopianairlines.pilot.data.model.Exam
    @SerializedName("_id") // Map JSON's "_id" to this field
    val id: String? = null,         // Make id nullable and default to null
    val title: String,
    val description: String,
    val category: String? = null,   // Make nullable to handle API responses with missing category
    val difficulty: String? = null, // Make nullable to handle API responses with missing difficulty
    val durationMinutes: Int,
    val isActive: Boolean,
//    val createdAt: String,  // Add this
//    val updatedAt: String,  // Add this
//    @SerializedName("__v")  // Map JSON's "__v"
//    val version: Int        // Add this (or whatever name you prefer for __v)
)
// Note: The fields like passingScore, totalQuestions, questions
// are NOT in the list item JSON you provided, so they are correctly omitted here.
// They are in your ExamDto, which is good for a detailed view.