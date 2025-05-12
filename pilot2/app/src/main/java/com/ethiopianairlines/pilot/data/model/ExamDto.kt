package com.ethiopianairlines.pilot.data.model

import com.ethiopianairlines.pilot.domain.model.Exam as DomainExam // Alias for clarity
import com.ethiopianairlines.pilot.domain.model.ExamCategory
import com.ethiopianairlines.pilot.domain.model.ExamDifficulty
import com.ethiopianairlines.pilot.domain.model.Question as DomainQuestion // Alias
import com.google.gson.annotations.SerializedName // Or Moshi's @Json

/**
 * Data Transfer Object for representing detailed Exam information,
 * typically received from an API endpoint like GET /exams/{id}.
 * It includes associated questions.
 */
data class ExamDto(
    @SerializedName("_id") // Assuming JSON key is _id
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,       // e.g., "pilotTrainee" from backend
    val difficulty: String? = null,     // e.g., "medium" from backend
    val durationMinutes: Int = 0,
    val passingScore: Int = 0,
    val totalQuestions: Int = 0,
    val isActive: Boolean = false,
    val questions: List<QuestionResponseDto>? = null, // Make nullable with null default
//    val createdAt: String,
//    val updatedAt: String,
//    @SerializedName("__v")      // If backend sends version
//    val version: Int? = null    // Nullable if not always present or not needed
) {
    /**
     * Maps this ExamDto (data layer) to a DomainExam (domain layer model).
     */
    fun toDomain(): DomainExam {
        // Determine domain category based on backend string, safely handling nulls
        val domainCategory = when (category?.lowercase()) {
            null -> {
                println("Warning: Null category received for exam ID '$id'. Defaulting.")
                ExamCategory.PILOT_TRAINEE.toString()
            }
            "pilottrainee" -> ExamCategory.PILOT_TRAINEE.toString()
            "flightinstructor" -> ExamCategory.PILOT_INSTRUCTOR.toString()
            "pilotexaminer" -> ExamCategory.PILOT_EXAMINER.toString()
            else -> {
                // Handle unknown category: default, log, or throw
                println("Warning: Unknown category '$category' received for exam ID '$id'. Defaulting.")
                ExamCategory.PILOT_TRAINEE.toString()
            }
        }

        // Determine domain difficulty based on backend string, safely handling nulls
        val domainDifficulty = when (difficulty?.lowercase()) {
            null -> {
                println("Warning: Null difficulty received for exam ID '$id'. Defaulting.")
                ExamDifficulty.MEDIUM.toString()
            }
            "easy" -> ExamDifficulty.EASY.toString()
            "medium" -> ExamDifficulty.MEDIUM.toString()
            "hard" -> ExamDifficulty.HARD.toString()
            else -> {
                println("Warning: Unknown difficulty '$difficulty' received for exam ID '$id'. Defaulting.")
                ExamDifficulty.MEDIUM.toString()
            }
        }

        return DomainExam(
            id = this.id ?: "",
            title = this.title ?: "",
            description = this.description ?: "",
            category = domainCategory,
            difficulty = domainDifficulty,
            durationMinutes = this.durationMinutes,
            passingScore = this.passingScore,
            totalQuestions = this.totalQuestions,
            questionCount = this.totalQuestions,
            isActive = this.isActive,
            // Safely map questions, handling null case
            questions = this.questions?.map { it.toDomainModel() } ?: emptyList(),
//            createdAt = this.createdAt,
//            updatedAt = this.updatedAt
        )
    }

    companion object {
        /**
         * Maps a DomainExam (domain layer model) to an ExamDto (data layer).
         * Useful if you need to send this detailed DTO as a request body for updates,
         * or for testing/preview purposes.
         */
        fun fromDomain(domainExam: DomainExam): ExamDto {
            // Map domain category string back to backend string
            val backendCategory = when (domainExam.category) {
                ExamCategory.PILOT_TRAINEE.toString() -> "pilotTrainee"
                ExamCategory.PILOT_INSTRUCTOR.toString() -> "flightInstructor"
                ExamCategory.PILOT_EXAMINER.toString() -> "pilotExaminer"
                else -> domainExam.category.lowercase() // Fallback or throw
            }

            // Map domain difficulty string back to backend string
            val backendDifficulty = when (domainExam.difficulty) {
                ExamDifficulty.EASY.toString() -> "easy"
                ExamDifficulty.MEDIUM.toString() -> "medium"
                ExamDifficulty.HARD.toString() -> "hard"
                else -> domainExam.difficulty.lowercase() // Fallback or throw
            }

            return ExamDto(
                id = domainExam.id,
                title = domainExam.title,
                description = domainExam.description,
                category = backendCategory,
                difficulty = backendDifficulty,
                durationMinutes = domainExam.durationMinutes,
                passingScore = domainExam.passingScore,
                totalQuestions = domainExam.totalQuestions, // Or domainExam.questions.size
                isActive = domainExam.isActive,
                // Map each DomainQuestion to QuestionResponseDto
                // This requires QuestionResponseDto to have a `fromDomain` or for you to use a QuestionMapper
                questions = domainExam.questions.map { domainQuestion ->
                    // Assuming QuestionResponseDto does NOT have a fromDomain.
                    // You'd typically not construct a full ResponseDTO to send to backend.
                    // If sending Question data, you'd use CreateQuestionRequestDto.
                    // This mapping is more for local transformation if needed.
                    // For now, let's assume a direct mapping if fields align (which they might not)
                    // OR use a QuestionMapper if you have one:
                    // QuestionMapper.fromDomainToResponseDto(domainQuestion) - needs implementation
                    // For now, let's imagine QuestionResponseDto needs to be constructed carefully:
                    QuestionResponseDto( // This is illustrative; you might not need to create QuestionResponseDto this way.
                        id = domainQuestion.id,
                        examId = domainExam.id, // Or domainQuestion.examId
                        text = domainQuestion.text,
                        options = domainQuestion.options,
                        correctAnswer = domainQuestion.correctAnswerIndex,
                        explanation = domainQuestion.explanation,
//                        createdAt = domainQuestion.createdAt, // This might be problematic if sending
//                        updatedAt = domainQuestion.updatedAt, // This might be problematic if sending
//                        version = null // Typically not sent from client
                    )
                },
//                createdAt = domainExam.createdAt, // Be cautious sending this for updates
//                updatedAt = domainExam.updatedAt, // Be cautious sending this for updates
//                version = null // Typically not sent for updates
            )
        }
    }
}