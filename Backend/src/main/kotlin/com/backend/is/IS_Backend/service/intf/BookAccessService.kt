package com.backend.`is`.IS_Backend.service.intf

import org.springframework.security.core.Authentication

interface BookAccessService {
    fun canAccess(bookId: Long, authentication: Authentication): Boolean
}
