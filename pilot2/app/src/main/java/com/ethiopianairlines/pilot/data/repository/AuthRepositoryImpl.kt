package com.ethiopianairlines.pilot.data.repository

import com.ethiopianairlines.pilot.data.remote.api.AuthApi
import com.ethiopianairlines.pilot.data.remote.api.RegisterRequest
import com.ethiopianairlines.pilot.data.remote.api.LoginRequest
import com.ethiopianairlines.pilot.domain.entity.User
import com.ethiopianairlines.pilot.domain.repository.AuthRepository
import retrofit2.HttpException

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {
    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<User> = try {
        val response = api.register(RegisterRequest(name, email, password, listOf("student")))
        Result.success(
            User(
                id = response.data.id,
                firstName = response.data.name,
                lastName = "",
                email = response.data.email,
                roles = response.data.roles
            )
        )
    } catch (e: HttpException) {
        when (e.code()) {
            409 -> Result.failure(Exception("This email is already registered. Please use a different email address."))
            else -> Result.failure(Exception("Registration failed: ${e.message}"))
        }
    } catch (e: Exception) {
        Result.failure(Exception("Registration failed: ${e.message}"))
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<Pair<User, String>> = try {
        val response = api.login(LoginRequest(email, password))
        val user = response.data.user
        val token = response.data.access_token
        Result.success(
            User(
                id = user.id,
                firstName = user.name,
                lastName = "",
                email = user.email,
                roles = user.roles
            ) to token
        )
    } catch (e: HttpException) {
        when (e.code()) {
            401 -> Result.failure(Exception("Invalid email or password."))
            else -> Result.failure(Exception("Login failed: ${e.message}"))
        }
    } catch (e: Exception) {
        Result.failure(Exception("Login failed: ${e.message}"))
    }
} 