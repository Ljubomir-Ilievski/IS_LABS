package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.Admin
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin, Long>
