package com.backend.`is`.IS_Backend.repository

import com.backend.`is`.IS_Backend.model.domain.Book
import com.backend.`is`.IS_Backend.model.domain.BookAccess
import com.backend.`is`.IS_Backend.model.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookAccessRepository : JpaRepository<BookAccess, Long> {
    fun findByUserAndBook(user: User, book: Book): Optional<BookAccess>
    fun findAllByUser(user: User): List<BookAccess>
}
