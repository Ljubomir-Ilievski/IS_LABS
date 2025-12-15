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
        return if (entry != null && entry.code == code && LocalDateTime.now().isBefore(entry.expiration)) {
            codeStorage.remove(email)
            jWTService.generateToken(user)
        } else {
            throw IllegalArgumentException("Invalid code");
        }
    }

    override fun verifyCodeOnly(email: String, code: Int): Boolean {
        val entry = codeStorage[email]
        return if (entry != null && entry.code == code && LocalDateTime.now().isBefore(entry.expiration)) {
            codeStorage.remove(email)
            true
        } else {
            false
        }
    }

    private data class CodeEntry(val code: Int, val expiration: LocalDateTime)
}
