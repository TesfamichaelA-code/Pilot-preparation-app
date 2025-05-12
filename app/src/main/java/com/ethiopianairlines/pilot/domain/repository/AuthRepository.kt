package com.ethiopianairlines.pilot.domain.repository

import com.ethiopianairlines.pilot.domain.entity.User

interface AuthRepository {
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<User>

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<Pair<User, String>>
} 