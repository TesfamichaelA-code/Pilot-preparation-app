package com.ethiopianairlines.pilot.data.repository

import com.ethiopianairlines.pilot.data.model.InterviewRequest
import com.ethiopianairlines.pilot.data.remote.api.InterviewApi
import javax.inject.Inject

class InterviewRepositoryImpl @Inject constructor(
    private val api: InterviewApi
) : InterviewRepository {
    override suspend fun createInterview(interview: InterviewRequest) {
        val response = api.createInterview(interview)
        if (!response.isSuccessful) {
            throw Exception("Failed to create interview: ${response.code()} ${response.message()}")
        }
    }
} 