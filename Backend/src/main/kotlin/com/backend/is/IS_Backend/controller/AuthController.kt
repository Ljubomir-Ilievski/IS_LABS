package com.backend.`is`.IS_Backend.controller



import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.AuthResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.CountResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.LoginDTO
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.service.intf.AuthServiceKt
import com.backend.`is`.IS_Backend.service.intf.TwoFactorAuthService
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
    private val authenticationService: AuthServiceKt,
    private val twoFactorAuthService: TwoFactorAuthService
) {

    @PostMapping("/register/admin")
    fun registerAdmin(@RequestBody request: AuthRequestDTO): ResponseEntity<Void> {
        authenticationService.registerAdmin(request)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PostMapping("/register/librarian")
    fun registerLibrarian(@RequestBody request: AuthRequestDTO): ResponseEntity<Void> {
        authenticationService.registerLibrarian(request)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PostMapping("/register/reader")
    fun registerReader(@RequestBody request: AuthRequestDTO): ResponseEntity<Void> {
        authenticationService.registerReader(request)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginDTO): ResponseEntity<AuthResponseDTO> {
       authenticationService.login(request)
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
