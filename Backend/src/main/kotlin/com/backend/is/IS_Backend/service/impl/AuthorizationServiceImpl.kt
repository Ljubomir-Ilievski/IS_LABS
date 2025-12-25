package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.dto.auth.JitAuthorisationRequest
import com.backend.`is`.IS_Backend.dto.auth.JitTokenResult
import com.backend.`is`.IS_Backend.enumerations.Roles
import com.backend.`is`.IS_Backend.exception.AdminElevationNotAllowedException
import com.backend.`is`.IS_Backend.exception.ForbiddenOperationException
import com.backend.`is`.IS_Backend.exception.InvalidTimeWindowException
import com.backend.`is`.IS_Backend.exception.UserNotFoundException
import com.backend.`is`.IS_Backend.model.domain.JitChange
import com.backend.`is`.IS_Backend.repository.JitChangeRepository
import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.AuthorizationService
import com.backend.`is`.IS_Backend.service.intf.JWTService
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class AuthorizationServiceImpl(
    private val jwtService: JWTService,
    private val userRepository: UserRepository,
    private val jitChangeRepository: JitChangeRepository
) : AuthorizationService {

    private fun validateJitRequestAndGetTarget(req: JitAuthorisationRequest): com.backend.`is`.IS_Backend.model.domain.User {
        val now = Instant.now()
        if (!req.validFrom.isBefore(req.validTo) || req.validTo.isBefore(now)) {
            throw InvalidTimeWindowException()
        }

        val target = userRepository.findByEmail(req.targetEmail).orElseThrow { UserNotFoundException() }
        val isTargetAdmin = target.authorities.any { it.authority == Roles.ROLE_ADMIN.name }
        if (isTargetAdmin) {
            throw ForbiddenOperationException("Cannot change roles of another admin")
        }

        if (req.targetRole == Roles.ROLE_ADMIN) {
            throw AdminElevationNotAllowedException()
        }

        return target
    }

    override fun createJitForRoleChange(req: JitAuthorisationRequest, requesterEmail: String): JitTokenResult {
        validateJitRequestAndGetTarget(req)

        val currentUser = userRepository.findByEmail(requesterEmail)
            .orElseThrow { ForbiddenOperationException("Authenticated user not found") }

        val issuedAt = Date.from(req.validFrom)
        val expiresAt = Date.from(req.validTo)
        val jitToken = jwtService.generateJitToken(currentUser, issuedAt, expiresAt, req.targetRole.name)

        return JitTokenResult(token = jitToken, expiresAt = req.validTo)
    }

    override fun changeRoleWithJit(req: JitAuthorisationRequest) {
        val target = validateJitRequestAndGetTarget(req)
        val newRoleChange = JitChange(null, req.targetEmail, req.targetRole, req.validFrom, req.validTo, target)
        jitChangeRepository.save(newRoleChange)
    }
}
