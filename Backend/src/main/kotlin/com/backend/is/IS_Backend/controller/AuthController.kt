package com.backend.`is`.IS_Backend.controller

import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.AuthResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.CountResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.LoginDTO
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.service.intf.AuthServiceKt
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthServiceKt
) {

    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequestDTO): ResponseEntity<AuthResponseDTO> {
        authenticationService.register(request)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginDTO, response: HttpServletResponse): ResponseEntity<AuthResponseDTO> {
        val token = authenticationService.login(request)

        val cookie = Cookie("jwt", token).apply {
            isHttpOnly = true
            secure = false
            path = "/"
            maxAge = 24 * 60 * 60
        }
        response.addCookie(cookie)
        return ResponseEntity( HttpStatus.OK)
    }

    @PostMapping("/makeCount")
    fun makeCount(@AuthenticationPrincipal user: User): ResponseEntity<CountResponseDTO> {
       return ResponseEntity(authenticationService.makeCount(user),HttpStatus.OK)
    }

    @GetMapping("/getCount")
    fun getCount(@AuthenticationPrincipal user: User): ResponseEntity<CountResponseDTO> {
        return ResponseEntity(authenticationService.getCount(user), HttpStatus.OK)
    }
}
