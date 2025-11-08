package com.backend.`is`.IS_Backend.dto.auth

data class AuthRequestDTO(
    val email: String,
    val password: String
)

data class LoginDTO(
    val email: String,
    val password: String
)

data class AuthResponseDTO(
    val email: String,
    val token: String
)

data class CountResponseDTO(
   val count: Int
)