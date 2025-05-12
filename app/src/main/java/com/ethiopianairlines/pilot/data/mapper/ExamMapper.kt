package com.ethiopianairlines.pilot.data.mapper

// DTO for list items / simple POST/PUT payloads
import com.ethiopianairlines.pilot.data.model.Exam as DataExam
// Domain model
import com.ethiopianairlines.pilot.domain.model.Exam as DomainExam
import com.ethiopianairlines.pilot.domain.model.ExamCategory
import com.ethiopianairlines.pilot.domain.model.ExamDifficulty

object ExamMapper {

    /**
     * Maps a DomainExam (domain layer model) to a DataExam (data layer DTO).
     * This DTO is assumed to be the structure expected by the backend for
     * creating or updating exams (e.g., simpler structure).
     */
    fun toDataModel(domainExam: DomainExam): DataExam {
        // Your DomainExam.category and DomainExam.difficulty are Strings that
        // are expected to match the enum.toString() values.
        // We need to map them back to the simple string values the backend expects.
        val backendCategory = when (domainExam.category) {
            ExamCategory.PILOT_TRAINEE.toString() -> "pilotTrainee"
            ExamCategory.PILOT_INSTRUCTOR.toString() -> "flightInstructor"
            ExamCategory.PILOT_EXAMINER.toString() -> "pilotExaminer" // Assuming this maps to "pilotexaminer" or similar
            // Add more mappings if necessary or a default/error handling
            else -> "pilotTrainee" // Or throw IllegalArgumentException("Unsupported category for backend: ${domainExam.category}")
        }

        val backendDifficulty = when (domainExam.difficulty) {
            ExamDifficulty.EASY.toString() -> "easy"
            ExamDifficulty.MEDIUM.toString() -> "medium"
            ExamDifficulty.HARD.toString() -> "hard"
            // Add more mappings if necessary or a default/error handling
            else -> "medium" // Or throw IllegalArgumentException("Unsupported difficulty for backend: ${domainExam.difficulty}")
        }

        return DataExam(
            // id for DataExam is String (non-null).
            // If creating a new exam, the backend might assign the ID, so you might send it as null or an empty string
            // depending on API spec. However, your DataExam has `id: String`.
            // For updates, domainExam.id will have the value. For creates, it might be empty.
            // The backend typically ignores the ID in the payload for POST / creates a new one.
            // Let's assume for POST, the ID in the DTO can be the domainExam.id (which might be empty if new)
            // or if your DataExam.id was nullable (e.g. `id: String? = null`), you could pass null for new items.
            // Given DataExam.id is `String`, we'll pass domainExam.id.
            // Backend should handle `_id` generation on POST. For PUT, `_id` in URL is primary.
            id = domainExam.id, // Or handle if id should be omitted/null for creation
            title = domainExam.title,
            description = domainExam.description,
            category = backendCategory,
            difficulty = backendDifficulty,
            durationMinutes = domainExam.durationMinutes,
            isActive = domainExam.isActive,
//            id = TODO(),
            // DataExam (the simple DTO) does not have passingScore, totalQuestions, questions.
            // It does have createdAt, updatedAt, and version from our previous correction.
            // When sending data for creation (POST), `createdAt`, `updatedAt`, `version` are usually
            // set by the backend. So, we either omit them from DataExam when sending,
            // or ensure DataExam has default/ignorable values for them if they are non-null.
            // Your corrected DataExam has them as non-null.
            // Let's assume for POST, we send empty strings or defaults if backend ignores them,
            // or the backend allows them to be part of the DTO.
            // If these are truly generated server-side and shouldn't be sent,
            // you might need another DTO for POST/PUT requests that omits them.
            // For simplicity, using the current DataExam structure:
//            createdAt = domainExam.createdAt.ifEmpty { "1970-01-01T00:00:00.000Z" }, // Placeholder if empty, backend should override
//            updatedAt = domainExam.updatedAt.ifEmpty { "1970-01-01T00:00:00.000Z" }, // Placeholder if empty, backend should override
//            version = 0 // Default version, backend should manage
        )
    }

    /**
     * Maps a DataExam (data layer DTO from list API) to a DomainExam (domain layer model).
     */
    fun toDomainModel(dataExam: DataExam): DomainExam {
        return DomainExam(
            id = dataExam.id,
            title = dataExam.title,
            description = dataExam.description,
            category = when (dataExam.category.lowercase()) {
                "pilottrainee" -> ExamCategory.PILOT_TRAINEE.toString()
                "flightinstructor" -> ExamCategory.PILOT_INSTRUCTOR.toString()
                "pilotexaminer" -> ExamCategory.PILOT_EXAMINER.toString() // Assuming "pilotexaminer" from backend
                else -> ExamCategory.PILOT_TRAINEE.toString() // Default or throw
            },
            difficulty = when (dataExam.difficulty.lowercase()) {
                "easy" -> ExamDifficulty.EASY.toString()
                "medium" -> ExamDifficulty.MEDIUM.toString()
                "hard" -> ExamDifficulty.HARD.toString()
                else -> ExamDifficulty.MEDIUM.toString() // Default or throw
            },
            durationMinutes = dataExam.durationMinutes,
            passingScore = 0, // Default value, not in list item DTO
            totalQuestions = 0, // Default value, not in list item DTO
            isActive = dataExam.isActive,
            questions = emptyList(), // Not present in list item DTO
//            createdAt = dataExam.createdAt,
//            updatedAt = dataExam.updatedAt
            // DomainExam does not have 'version' (__v) field, which is fine.
        )
    }
}