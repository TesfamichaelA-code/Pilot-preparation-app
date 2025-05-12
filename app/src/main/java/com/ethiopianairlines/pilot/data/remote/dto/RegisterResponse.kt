package com.ethiopianairlines.pilot.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val data: UserData,
    val status: String,
    val timestamp: String
)

data class UserData(
    val name: String,
    val email: String,
    val roles: List<String>,
    @SerializedName("_id")
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("__v")
    val version: Int
) 