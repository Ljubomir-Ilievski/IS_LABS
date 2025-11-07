package com.backend.`is`.IS_Backend.service.intf

import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.AuthResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.LoginDTO

interface AuthServiceKt {
    fun register(request: AuthRequestDTO): AuthResponseDTO
    fun login(request: LoginDTO): AuthResponseDTO
}