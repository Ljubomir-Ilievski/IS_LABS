package com.backend.`is`.IS_Backend.model.domain

import com.backend.`is`.IS_Backend.enumerations.Roles
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
class Admin(
    email: String = "",
    password: String = ""
) : User(email, password, Roles.ROLE_ADMIN)
