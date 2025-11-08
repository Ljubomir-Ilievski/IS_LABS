package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.CountResponseDTO
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
    override fun register(request: AuthRequestDTO): Unit {
        // Ensure email is unique
        if (userRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("Email is already in use")
        }
        val user = User(request.email, passwordEncoder.encode(request.password))
        userRepository.save(user)
    }

    override fun login(request: LoginDTO) : String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email).orElseThrow {
            IllegalArgumentException("Invalid credentials")
        }
        return jwtService.generateToken(user)
    }
    override fun makeCount(user: User) : CountResponseDTO {
        user.setCount(user.getCount() + 1)
        userRepository.save(user)
        return CountResponseDTO(user.getCount());
    }

    override fun getCount(user: User) : CountResponseDTO {
        return CountResponseDTO(user.getCount())
    }
}