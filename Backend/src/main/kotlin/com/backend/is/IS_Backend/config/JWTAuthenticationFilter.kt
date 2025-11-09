package com.backend.`is`.IS_Backend.config

import com.backend.`is`.IS_Backend.service.intf.JWTService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthenticationFilter(
    private val jwtService: JWTService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Extract JWT from cookie
        val token = request.cookies?.firstOrNull { it.name == "jwt" }?.value

        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                // Extract username from token
                val username = jwtService.extractUsername(token)

                // Load user details
                val userDetails = userDetailsService.loadUserByUsername(username)

                // Validate token
                if (jwtService.isTokenValid(token)) {
                    // Create authentication token
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // Set authentication in security context
                    SecurityContextHolder.getContext().authentication = authToken
                }
            } catch (e: Exception) {
                // Token is invalid, continue without authentication
                logger.error("JWT validation failed: ${e.message}")
            }
        }

        filterChain.doFilter(request, response)
    }
}