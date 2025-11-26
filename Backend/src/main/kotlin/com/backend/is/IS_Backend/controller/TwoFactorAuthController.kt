package com.backend.`is`.IS_Backend.controller



import  com.backend.`is`.IS_Backend.service.intf.TwoFactorAuthService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/2fa")
class TwoFactorAuthController(private val twoFactorAuthServiceImpl: TwoFactorAuthService) {

    @PostMapping("/send")
    fun sendCode(@RequestParam email: String): String {
        twoFactorAuthServiceImpl.sendCode(email)
        return "Code sent to $email"
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestParam email: String, @RequestParam code: Int): String {
        return if (twoFactorAuthServiceImpl.verifyCode(email, code)) {
            "2FA successful"
        } else {
            "Invalid or expired code"
        }
    }
}
