package com.ethiopianairlines.pilot.data.repository

import com.ethiopianairlines.pilot.data.model.InterviewRequest

interface InterviewRepository {
    suspend fun createInterview(interview: InterviewRequest)
} 