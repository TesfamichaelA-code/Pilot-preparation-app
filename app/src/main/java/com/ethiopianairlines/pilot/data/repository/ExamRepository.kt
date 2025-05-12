package com.ethiopianairlines.pilot.data.repository

import com.ethiopianairlines.pilot.domain.model.Exam
//import com.ethiopianairlines.pilot.domain.model.Exam
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    suspend fun createExam(exam: Exam)
    fun getExams(): Flow<List<Exam>>
    suspend fun getExamById(id: String): Exam?
    suspend fun updateExam(exam: Exam)
    suspend fun deleteExam(id: String)
} 