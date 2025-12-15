package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.Librarian
import org.springframework.data.jpa.repository.JpaRepository

interface LibrarianRepository : JpaRepository<Librarian, Long>
