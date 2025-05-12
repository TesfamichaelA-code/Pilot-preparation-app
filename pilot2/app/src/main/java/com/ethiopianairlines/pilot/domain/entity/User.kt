package com.ethiopianairlines.pilot.domain.entity

data class User(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null,
    val roles: List<String> = emptyList()
) 