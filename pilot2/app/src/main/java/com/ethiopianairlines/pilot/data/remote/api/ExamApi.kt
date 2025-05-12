package com.ethiopianairlines.pilot.data.remote.api

// Import the DTO used for individual items and the new wrapper
import com.ethiopianairlines.pilot.data.model.AddQuestionApiResponse
import com.ethiopianairlines.pilot.data.model.ApiResponse
import com.ethiopianairlines.pilot.data.model.CreateQuestionRequestDto
import com.ethiopianairlines.pilot.data.model.Exam // This is data.model.Exam
import com.ethiopianairlines.pilot.data.model.ExamListApiResponse // Your new wrapper
import com.ethiopianairlines.pilot.data.model.ExamDto // For single exam details if different
import com.ethiopianairlines.pilot.data.model.ExamResultApiResponse
import com.ethiopianairlines.pilot.data.model.ExamResultListApiResponse
import com.ethiopianairlines.pilot.data.model.QuestionListApiResponse
import com.ethiopianairlines.pilot.data.model.QuestionResponseDto
import retrofit2.http.*

// ExamSubmissionRequest class for bundling submission data
data class ExamSubmissionRequest(
    val answers: Map<String, Int>,
    val score: Int
)

interface ExamApi {
    // For createExam: The @Body exam: Exam should likely be ExamDto if that's the full structure
    // you send. The return type also might be ExamDto or a wrapper.
    // Let's assume for now it's the simple data.model.Exam for create/update requests
    // if the backend expects a simpler payload for POST/PUT.
    // If it expects the full ExamDto structure, change Exam to ExamDto.
    @POST("exams")
    suspend fun createExam(@Body exam: com.ethiopianairlines.pilot.data.model.Exam): com.ethiopianairlines.pilot.data.model.Exam // Or ExamDto

    @GET("exams")
    suspend fun getExams(
        @Query("category") category: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): ExamListApiResponse

    // For getExamById: If this endpoint returns the more detailed structure (with questions etc.),
    // then its return type should be ExamDto (the one with questions).
    @GET("exams/{id}")
    suspend fun getExamById(@Path("id") id: String): com.ethiopianairlines.pilot.data.model.ExamDto // <-- Use ExamDto if it's more detailed

    // For updateExam: Similar to createExam, consider if @Body should be ExamDto
    @PUT("exams/{id}")
    suspend fun updateExam(@Path("id") id: String, @Body exam: com.ethiopianairlines.pilot.data.model.Exam): com.ethiopianairlines.pilot.data.model.Exam // Or ExamDto

    @DELETE("exams/{id}")
    suspend fun deleteExam(@Path("id") id: String) // No body, no return typically or simple success/failure

    @POST("exams/{examId}/questions")
    suspend fun addQuestionToExam(
        @Path("examId") examId: String,
        @Body questionDto: CreateQuestionRequestDto
    ): AddQuestionApiResponse // Assuming API returns the created question

    @GET("exams/{examId}/questions")
    suspend fun getQuestionsForExam(
        @Path("examId") examId: String
    ): QuestionListApiResponse// Assuming direct list, or QuestionListApiResponse if wrapped

    @POST("progress/exams/{examId}/submit")
    suspend fun submitExamResults(
        @Path("examId") examId: String,
        @Body request: ExamSubmissionRequest
    ): ApiResponse<Boolean>

    @GET("exams/categories")
    suspend fun getExamCategories(@Query("fetch") fetch: Boolean = true): ApiResponse<List<String>>

    @GET("progress/exams")
    suspend fun getUserExamHistory(): ExamResultListApiResponse

    @GET("progress/exams/{examId}")
    suspend fun getUserExamResult(
        @Path("examId") examId: String
    ): ExamResultApiResponse
}