package com.ethiopianairlines.pilot.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: LoginData,
    val status: String,
    val timestamp: String
)

data class LoginData(
    val user: LoginUser,
    val access_token: String
)

data class LoginUser(
    @SerializedName("_id")
    val id: String,
    val email: String,
    val name: String,
    val roles: List<String>
) 