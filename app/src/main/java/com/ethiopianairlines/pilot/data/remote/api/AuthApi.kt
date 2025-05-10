package com.ethiopianairlines.pilot.data.remote.api

import com.ethiopianairlines.pilot.data.remote.dto.RegisterResponse
import com.ethiopianairlines.pilot.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

// Data classes for request/response

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val roles: List<String>
)

data class LoginRequest(
    val email: String,
    val password: String
)

interface AuthApi {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
} 