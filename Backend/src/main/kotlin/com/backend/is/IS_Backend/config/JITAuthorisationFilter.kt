package com.backend.`is`.IS_Backend.config

import com.backend.`is`.IS_Backend.dto.auth.JitAuthorisationRequest
import com.backend.`is`.IS_Backend.exception.UserNotFoundException
import com.backend.`is`.IS_Backend.model.domain.JitChange
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.repository.JitChangeRepository
import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.JWTService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant
import kotlin.jvm.optionals.getOrNull


@Component
class JITAuthorisationFilter(private val jitChangeRepository: JitChangeRepository, private val userRepository: UserRepository) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val currentSecurityContext : SecurityContext = SecurityContextHolder.getContext()

        val currentAuthentication = currentSecurityContext.authentication

        val userEmail = currentAuthentication?.name
        var user : User? = null
         if (userEmail != null) {
             user = userRepository.findByEmail(userEmail!!).getOrNull()
         }
        var jitChange : JitChange? = null;
        if (user != null) {
             jitChange = jitChangeRepository.findJitChangeByUserId(user.id)
        }
        if (jitChange != null && validateJitRequest(jitChange)) {

            val merged: MutableCollection<GrantedAuthority?> =
                HashSet(currentAuthentication.authorities)

            merged.add(SimpleGrantedAuthority(jitChange.targetRole.name))

            val elevated: Authentication =
                UsernamePasswordAuthenticationToken(
                    currentAuthentication.principal,
                    currentAuthentication.credentials,
                    merged
                )

            SecurityContextHolder.getContext().authentication = elevated

        }

        filterChain.doFilter(request, response)
    }
    private fun validateJitRequest(req: JitChange) : Boolean {
        val now = Instant.now()
        return req.validFrom.isBefore(now) && req.validTo.isAfter(now)
    }
}