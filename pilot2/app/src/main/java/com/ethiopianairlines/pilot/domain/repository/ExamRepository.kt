package com.ethiopianairlines.pilot.domain.repository

// Import your DOMAIN model for Exam
import com.ethiopianairlines.pilot.domain.model.Exam // <-- CORRECT IMPORT
import com.ethiopianairlines.pilot.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    // All method signatures now use com.ethiopianairlines.pilot.domain.model.Exam
    suspend fun createExam(exam: Exam)
    fun getExams(): Flow<List<Exam>>
    suspend fun getExamById(id: String): Exam?
    suspend fun updateExam(exam: Exam)
    suspend fun deleteExam(id: String)
    suspend fun addQuestionToExam(examId: String, question: Question): Question // Return created domain Question
    fun getQuestionsForExam(examId: String): Flow<List<Question>>
    suspend fun getExams(category: String? = null, difficulty: String? = null): List<Exam>
    suspend fun getExamQuestions(examId: String): List<Question>
    suspend fun submitExamResults(examId: String, answers: Map<String, Int>, score: Int): Result<Boolean>
    suspend fun getExamCategories(): List<String>
    suspend fun getUserExamHistory(): List<ExamResult>
    suspend fun getUserExamResult(examId: String): ExamResult?
}

data class ExamResult(
    val examId: String,
    val examTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val completedAt: String,
    val isPassed: Boolean
)