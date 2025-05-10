package com.ethiopianairlines.pilot.domain.usecase

import com.ethiopianairlines.pilot.domain.entity.User
import com.ethiopianairlines.pilot.domain.repository.AuthRepository

class LoginUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Pair<User, String>> = repository.loginUser(email, password)
} 