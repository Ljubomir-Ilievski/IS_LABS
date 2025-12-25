package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    @Query("select u from User u where u.userRole <> com.backend.is.IS_Backend.enumerations.Roles.ROLE_ADMIN")
    fun findAllNonAdmin(): List<User>
}
