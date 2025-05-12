package com.ethiopianairlines.pilot.data.repository

// Import the DOMAIN model and alias it if you prefer, or use it directly
import android.util.Log
import com.ethiopianairlines.pilot.domain.model.Exam as DomainExam
import com.ethiopianairlines.pilot.data.mapper.ExamMapper
import com.ethiopianairlines.pilot.data.mapper.QuestionMapper
import com.ethiopianairlines.pilot.data.model.AddQuestionApiResponse
import com.ethiopianairlines.pilot.data.remote.api.ExamApi
// Import the corrected DOMAIN repository interface
import com.ethiopianairlines.pilot.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import com.ethiopianairlines.pilot.domain.model.Question as DomainQuestion
// Import DATA DTOs
import com.ethiopianairlines.pilot.data.model.Exam as DataExam // DTO for list items from /exams
import com.ethiopianairlines.pilot.data.model.ExamDto // DTO for detailed exam from /exams/{id}
import com.ethiopianairlines.pilot.data.model.QuestionListApiResponse
import com.ethiopianairlines.pilot.data.model.QuestionResponseDto

class ExamRepositoryImpl @Inject constructor(
    private val examApi: ExamApi
    // ExamMapper is an object, so direct usage is fine. No need to inject if it's an object.
) : ExamRepository { // Implements the corrected domain.repository.ExamRepository

    override suspend fun createExam(exam: DomainExam) { // Parameter is DomainExam
        try {
            // Map DomainExam to the DTO your API expects for creation (DataExam or ExamDto)
            // Your ExamMapper.toDataModel maps DomainExam to DataExam (the simpler DTO)
            val dataExamToCreate = ExamMapper.toDataModel(exam)
            examApi.createExam(dataExamToCreate) // examApi.createExam expects data.model.Exam (DataExam)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw Exception("Failed to create exam: ${e.message()}. Error body: $errorBody")
        } catch (e: Exception) {
            throw Exception("Failed to create exam: ${e.message}")
        }
    }

    override fun getExams(): Flow<List<DomainExam>> = flow { // Returns Flow of DomainExam list
        try {
            val apiResponse = examApi.getExams() // Returns ExamListApiResponse
            val dataExamsList: List<DataExam> = apiResponse.data  // This is List<data.model.Exam>
            // Map the list of DTOs (data.model.Exam) to Domain Models
            val domainExamsList = dataExamsList.map { dataExam ->
                ExamMapper.toDomainModel(dataExam)
            }
            emit(domainExamsList)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw Exception("Failed to get exams: ${e.message()}. Error body: $errorBody")
        } catch (e: Exception) {
            throw Exception("Failed to get exams: ${e.message}")
        }
    }

    override suspend fun getExamById(id: String): DomainExam? { // Returns nullable DomainExam
        return try {
            val examDto: ExamDto = examApi.getExamById(id) // examApi.getExamById returns data.model.ExamDto
            examDto.toDomain() // ExamDto has a toDomain() method
        } catch (e: HttpException) {
            if (e.code() == 404) return null // Gracefully handle Not Found
            val errorBody = e.response()?.errorBody()?.string()
            throw Exception("Failed to get exam by ID: ${e.message()}. Error body: $errorBody")
        } catch (e: Exception) {
            throw Exception("Failed to get exam by ID: ${e.message}")
        }
    }

    override suspend fun updateExam(exam: DomainExam) { // Parameter is DomainExam
        try {
            val examId = exam.id // DomainExam.id should be reliable
            if (examId.isBlank()) { // Check if domain model's ID is valid for an update
                throw IllegalArgumentException("Exam ID cannot be blank for update operation.")
            }
            // Map DomainExam to the DTO your API expects for update (DataExam or ExamDto)
            val dataExamToUpdate = ExamMapper.toDataModel(exam)
            examApi.updateExam(examId, dataExamToUpdate) // examApi.updateExam expects data.model.Exam
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw Exception("Failed to update exam: ${e.message()}. Error body: $errorBody")
        } catch (e: Exception) {
            throw Exception("Failed to update exam: ${e.message}")
        }
    }

    override suspend fun deleteExam(id: String) {
        try {
            examApi.deleteExam(id)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw Exception("Failed to delete exam: ${e.message()}. Error body: $errorBody")
        } catch (e: Exception) {
            throw Exception("Failed to delete exam: ${e.message}")
        }
    }

    override suspend fun addQuestionToExam(examId: String, question: DomainQuestion): DomainQuestion {
        try {
            val requestDto = QuestionMapper.fromDomainToCreateRequestDto(question)
            val apiResponse: AddQuestionApiResponse = examApi.addQuestionToExam(examId, requestDto) // Get the wrapped response
            val responseDto: QuestionResponseDto = apiResponse.data // Extract the QuestionResponseDto
            return QuestionMapper.toDomain(responseDto) // Or responseDto.toDomainModel()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            // Log the full errorBody to see what the server actually sent on error
            Log.e("ExamRepoImpl", "HttpException adding question: $errorBody", e)
            throw Exception("Failed to add question to exam ${examId}: ${e.code()} ${e.message()}. Error: $errorBody")
        } catch (e: Exception) {
            // Catch other exceptions (like deserialization errors if structure is still off)
            Log.e("ExamRepoImpl", "Exception adding question", e)
            throw Exception("Failed to add question to exam ${examId}: ${e.message}")
        }
    }

    override fun getQuestionsForExam(examId: String): Flow<List<DomainQuestion>> = flow {
        try {
            val apiResponse: QuestionListApiResponse = examApi.getQuestionsForExam(examId) // Get wrapped response
            val responseDtos: List<QuestionResponseDto> = apiResponse.data // Extract the list from 'data' field
            emit(responseDtos.map { QuestionMapper.toDomain(it) }) // Or it.toDomainModel()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("ExamRepoImpl", "HttpException getting questions: $errorBody", e)
            throw Exception("Failed to get questions for exam ${examId}: ${e.code()} ${e.message()}. Error: $errorBody")
        } catch (e: Exception) {
            // Catch other exceptions (like deserialization errors if structure is still off)
            Log.e("ExamRepoImpl", "Exception getting questions", e)
            throw Exception("Failed to get questions for exam ${examId}: ${e.message}")
        }
    }
}