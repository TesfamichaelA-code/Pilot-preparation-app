package com.ethiopianairlines.pilot.data.model

data class ApiResponse<T>(
    val status: String,
    val data: T,
    val timestamp: String
) 