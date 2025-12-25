package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.JWTService
import com.backend.`is`.IS_Backend.service.intf.TwoFactorAuthService
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Service
class TwoFactorAuthServiceImpl(
    private val mailSender: JavaMailSender,
    private val jWTService: JWTService,
    private val userRepository: UserRepository
) : TwoFactorAuthService {

    private val codeStorage = ConcurrentHashMap<String, CodeEntry>()
    private val DEV_READER_EMAIL = "reader@seed.local"
    private val DEV_OVERRIDE_CODE = 0 // Accept "000000" as dev override (parsed as Int -> 0)

    override fun sendCode(email: String) {
        val code = Random.nextInt(100000, 999999)
        val expiration = LocalDateTime.now().plusMinutes(5)
        codeStorage[email] = CodeEntry(code, expiration)

        val message = SimpleMailMessage().apply {
            setTo(email)
            subject = "Your 2FA Code"
            text = "Your verification code is: $code"
        }

        mailSender.send(message)
    }

    override fun verifyCode(email: String, code: Int): String {
        val entry = codeStorage[email]
        val user = userRepository.findByEmail(email).orElseThrow { IllegalArgumentException("Invalid email") };
        // Dev bypass for the seeded reader account
        if (email.equals(DEV_READER_EMAIL, ignoreCase = true) && code == DEV_OVERRIDE_CODE) {
            return jWTService.generateToken(user)
        }
        return if (entry != null && entry.code == code && LocalDateTime.now().isBefore(entry.expiration)) {
            codeStorage.remove(email)
            jWTService.generateToken(user)
        } else {
            throw IllegalArgumentException("Invalid code");
        }
    }

    override fun verifyCodeOnly(email: String, code: Int): Boolean {
        val entry = codeStorage[email]
        // Dev bypass for the seeded reader account
        if (email.equals(DEV_READER_EMAIL, ignoreCase = true) && code == DEV_OVERRIDE_CODE) {
            return true
        }
        return if (entry != null && entry.code == code && LocalDateTime.now().isBefore(entry.expiration)) {
            codeStorage.remove(email)
            true
        } else {
            false
        }
    }

    private data class CodeEntry(val code: Int, val expiration: LocalDateTime)
}
