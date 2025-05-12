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
}