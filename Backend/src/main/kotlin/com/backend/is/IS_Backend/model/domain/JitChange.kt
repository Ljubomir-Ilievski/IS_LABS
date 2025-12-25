package com.backend.`is`.IS_Backend.model.domain

import com.backend.`is`.IS_Backend.enumerations.Roles
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.time.Instant


@Entity
class JitChange(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val targetEmail: String,
    @Enumerated(EnumType.STRING)
    val targetRole: Roles,
    val validFrom: Instant,
    val validTo: Instant,
    @OneToOne
    val user: User? = null
) {





}