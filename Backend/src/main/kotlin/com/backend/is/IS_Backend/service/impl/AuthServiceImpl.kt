package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.AuthResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.LoginDTO
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.AuthServiceKt
import com.backend.`is`.IS_Backend.service.intf.JWTService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JWTService
) : AuthServiceKt {

    @Transactional
    override fun register(request: AuthRequestDTO): AuthResponseDTO {
        // Ensure email is unique
        if (userRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("Email is already in use")
        }
        val user = User(request.email, passwordEncoder.encode(request.password))
        val saved = userRepository.save(user)
        val token = jwtService.generateToken(saved)
        return AuthResponseDTO(email = saved.username, token = token)
    }

    override fun login(request: LoginDTO): AuthResponseDTO {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email).orElseThrow {
            IllegalArgumentException("Invalid credentials")
        }
        val token = jwtService.generateToken(user)
        return AuthResponseDTO(email = user.username, token = token)
    }
}