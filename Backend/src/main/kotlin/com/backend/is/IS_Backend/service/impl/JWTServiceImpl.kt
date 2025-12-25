package com.backend.`is`.IS_Backend.service.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import com.backend.`is`.IS_Backend.constants.JWTConstants
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.service.intf.JWTService
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date
import java.util.function.Function

@Service
class JWTServiceImpl : JWTService {
    override fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)

    override fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body

    override fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(JWTConstants.SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    override fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    override fun generateToken(user: User): String =
        Jwts.builder()
            .setSubject(user.username)
            .claim("id", user.id)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + JWTConstants.EXPIRATION_TIME))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()

    override fun generateJitToken(user: User, willBeIssuedAt: Date, expiresAt: Date, roleToBeElevated: String): String =
        Jwts.builder()
            .setSubject(user.username)
            .claim("id", user.id)
            .claim("jit_role", roleToBeElevated)
            .setIssuedAt(willBeIssuedAt)
            .setExpiration(expiresAt)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()


    override fun isTokenValid(token: String): Boolean {
        extractAllClaims(token);
        return !isTokenExpired(token)
    }

    override fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())
    override fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)
    override fun extractIssuedAt(token: String) : Date = extractClaim(token, Claims::getIssuedAt)
    override fun extractElevatedRole(token: String): String ?{
        val claims = extractAllClaims(token)
        return claims["jit_role"] as? String
    }

    override fun isJitTokenValid(token: String): Boolean {
        val now = Date()
        return now.before(extractExpiration(token)) &&
                now.after(extractIssuedAt(token))
    }
}
