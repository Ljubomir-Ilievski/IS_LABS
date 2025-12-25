package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long>
