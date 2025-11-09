package com.backend.`is`.IS_Backend.service.intf

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.util.Date
import java.util.function.Function
import com.backend.`is`.IS_Backend.model.domain.User

interface JWTService {
    fun extractUsername(token: String): String
    fun extractAllClaims(token: String): Claims
    fun getSignInKey(): Key
    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T
    fun generateToken(user: User): String
    fun isTokenValid(token: String): Boolean
    fun isTokenExpired(token: String): Boolean
    fun extractExpiration(token: String): Date
}