package com.backend.`is`.IS_Backend.controller



import com.backend.`is`.IS_Backend.service.intf.AuthServiceKt
import com.backend.`is`.IS_Backend.service.intf.JWTService
import  com.backend.`is`.IS_Backend.service.intf.TwoFactorAuthService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/2fa")
class TwoFactorAuthController(
    private val twoFactorAuthServiceImpl: TwoFactorAuthService,
    private val authenticationService: AuthServiceKt,
    private val jWTService: JWTService,) {

    @PostMapping("/send")
    fun sendCode(@RequestParam email: String): String {
        twoFactorAuthServiceImpl.sendCode(email)
        return "Code sent to $email"
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestParam email: String, @RequestParam code: Int, response: HttpServletResponse): String {
        val token = twoFactorAuthServiceImpl.verifyCode(email, code)

        val cookie = Cookie("jwt", token).apply {
            isHttpOnly = true
            secure = false
            path = "/"
            maxAge = 24 * 60 * 60
        }
        response.addCookie(cookie)

        return "Login Successful"
    }
}
