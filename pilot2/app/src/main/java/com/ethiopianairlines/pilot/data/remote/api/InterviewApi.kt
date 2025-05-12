package com.ethiopianairlines.pilot.data.remote.api

import com.ethiopianairlines.pilot.data.model.InterviewRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface InterviewApi {
    @POST("interviews")
    suspend fun createInterview(@Body interview: InterviewRequest): retrofit2.Response<Unit>
} 