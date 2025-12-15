package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.Reader
import org.springframework.data.jpa.repository.JpaRepository

interface ReaderRepository : JpaRepository<Reader, Long>
