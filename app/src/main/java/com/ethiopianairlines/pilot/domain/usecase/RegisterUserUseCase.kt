package com.ethiopianairlines.pilot.domain.usecase

import com.ethiopianairlines.pilot.domain.entity.User
import com.ethiopianairlines.pilot.domain.repository.AuthRepository

class RegisterUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<User> = repository.registerUser(name, email, password)
} 