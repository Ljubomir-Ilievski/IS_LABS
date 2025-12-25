package com.backend.`is`.IS_Backend.service.intf

import com.backend.`is`.IS_Backend.dto.auth.JitAuthorisationRequest
import com.backend.`is`.IS_Backend.dto.auth.JitTokenResult

interface AuthorizationService {
    fun createJitForRoleChange(req: JitAuthorisationRequest, requesterEmail: String): JitTokenResult
    fun changeRoleWithJit(req: JitAuthorisationRequest);
}
