package com.backend.`is`.IS_Backend.service.intf

import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.AuthResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.CountResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.LoginDTO
import com.backend.`is`.IS_Backend.model.domain.User
import jakarta.servlet.http.HttpServletResponse

interface AuthServiceKt {
    fun registerAdmin(request: AuthRequestDTO)
    fun registerLibrarian(request: AuthRequestDTO)
    fun registerReader(request: AuthRequestDTO)
    fun verifyRegister(email: String, code: Int)
    fun login(request: LoginDTO) : String
    fun makeCount(user: User) : CountResponseDTO
    fun getCount(user: User) : CountResponseDTO
}