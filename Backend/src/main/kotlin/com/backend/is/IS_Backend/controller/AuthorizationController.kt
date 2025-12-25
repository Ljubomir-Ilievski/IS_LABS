package com.backend.`is`.IS_Backend.controller

import com.backend.`is`.IS_Backend.dto.auth.JitAuthorisationRequest
import com.backend.`is`.IS_Backend.service.intf.AuthorizationService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant

@RestController
@RequestMapping("/api/authorization")
class AuthorizationController(
    private val authorizationService: AuthorizationService
) {

    @PostMapping("/jit")
    fun createJitCookie(
        @RequestBody req: JitAuthorisationRequest,
        response: HttpServletResponse,
        auth: Authentication
    ): ResponseEntity<Any> {
        val now = Instant.now()
        val result = authorizationService.createJitForRoleChange(req, auth.name)

        val cookie = Cookie("jit", result.token).apply {
            isHttpOnly = true
            secure = true
            path = "/"
            val maxAgeSeconds = Duration.between(now, result.expiresAt).seconds
            maxAge = if (maxAgeSeconds > 0) maxAgeSeconds.toInt() else 0
        }
        response.addCookie(cookie)

        return ResponseEntity.ok(mapOf("status" to "jit_issued"))
    }

    @PostMapping("/jit-db")
    fun changeRoleJit(@RequestBody req: JitAuthorisationRequest) : ResponseEntity<Any> {
        authorizationService.changeRoleWithJit(req);
        return ResponseEntity.ok(mapOf("status" to "jit_issued"))
    }
}
