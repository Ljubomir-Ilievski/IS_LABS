package com.backend.`is`.IS_Backend.dto.auth

import com.backend.`is`.IS_Backend.enumerations.Roles
import java.time.Instant

data class JitAuthorisationRequest(
    val targetEmail: String,
    val targetRole: Roles,
    val validFrom: Instant,
    val validTo: Instant
)

data class JitTokenResult(
    val token: String,
    val expiresAt: Instant
)
