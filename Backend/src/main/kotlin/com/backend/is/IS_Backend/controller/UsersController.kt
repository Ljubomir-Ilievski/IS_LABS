package com.backend.`is`.IS_Backend.controller

import com.backend.`is`.IS_Backend.dto.user.UserSummary
import com.backend.`is`.IS_Backend.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val userRepository: UserRepository
) {

    @GetMapping("/non-admins")
    fun getNonAdminUsers(): List<UserSummary> =
        userRepository.findAllNonAdmin().map { u ->
            val role = u.authorities.firstOrNull()?.authority ?: "UNKNOWN"
            UserSummary(
                id = u.id ?: -1,
                email = u.username,
                role = role
            )
        }
}
