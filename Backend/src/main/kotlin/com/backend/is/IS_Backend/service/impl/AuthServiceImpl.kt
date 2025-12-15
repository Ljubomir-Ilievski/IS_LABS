package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.dto.auth.AuthRequestDTO
import com.backend.`is`.IS_Backend.dto.auth.CountResponseDTO
import com.backend.`is`.IS_Backend.dto.auth.LoginDTO
import com.backend.`is`.IS_Backend.enumerations.Roles
import com.backend.`is`.IS_Backend.exception.EmailAlreadyInUseException
import com.backend.`is`.IS_Backend.exception.InvalidEmailFormatException
import com.backend.`is`.IS_Backend.exception.WeakPasswordException
import com.backend.`is`.IS_Backend.model.domain.Admin
import com.backend.`is`.IS_Backend.model.domain.Librarian
import com.backend.`is`.IS_Backend.model.domain.Reader
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.AuthServiceKt
import com.backend.`is`.IS_Backend.service.intf.JWTService
import com.backend.`is`.IS_Backend.service.intf.TwoFactorAuthService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JWTService,
    private val twoFactorAuthService: TwoFactorAuthService
) : AuthServiceKt {

    val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val STRONG_PASSWORD_REGEX = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$")

    private val pendingRegistrations = ConcurrentHashMap<String, User>()

    private fun validateCredentials(email: String, password: String) {
        if (!EMAIL_REGEX.matches(email)) {
            throw InvalidEmailFormatException("Email must be a valid email address")
        }
        if (!STRONG_PASSWORD_REGEX.matches(password)) {
            throw WeakPasswordException("Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
        }
        if (userRepository.findByEmail(email).isPresent || pendingRegistrations.containsKey(email)) {
            throw EmailAlreadyInUseException()
        }
    }

    @Transactional
    override fun registerAdmin(request: AuthRequestDTO) {
        validateCredentials(request.email, request.password)
        val user = Admin(request.email, passwordEncoder.encode(request.password))
        pendingRegistrations[request.email] = user
        twoFactorAuthService.sendCode(request.email)
    }

    @Transactional
    override fun registerLibrarian(request: AuthRequestDTO) {
        validateCredentials(request.email, request.password)
        val user = Librarian(request.email, passwordEncoder.encode(request.password))
        pendingRegistrations[request.email] = user
        twoFactorAuthService.sendCode(request.email)
    }

    @Transactional
    override fun registerReader(request: AuthRequestDTO) {
        validateCredentials(request.email, request.password)
        val user = Reader(request.email, passwordEncoder.encode(request.password))
        pendingRegistrations[request.email] = user
        twoFactorAuthService.sendCode(request.email)
    }

    @Transactional
    override fun verifyRegister(email: String, code: Int) {
        val pending = pendingRegistrations[email] ?: throw IllegalArgumentException("No pending registration for email")
        val ok = twoFactorAuthService.verifyCodeOnly(email, code)
        if (!ok) throw IllegalArgumentException("Invalid code")
        userRepository.save(pending)
        pendingRegistrations.remove(email)
    }

    override fun login(request: LoginDTO) : String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email).orElseThrow {
            IllegalArgumentException("Invalid credentials")
        }

        twoFactorAuthService.sendCode(request.email)
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