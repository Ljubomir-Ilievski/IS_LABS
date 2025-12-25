package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.enumerations.AccessLevel
import com.backend.`is`.IS_Backend.model.domain.Book
import com.backend.`is`.IS_Backend.model.domain.BookAccess
import com.backend.`is`.IS_Backend.model.domain.User
import com.backend.`is`.IS_Backend.repository.BookAccessRepository
import com.backend.`is`.IS_Backend.repository.BookRepository
import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.BookAccessService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service("bookAccessService")
class BookAccessServiceImpl(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val bookAccessRepository: BookAccessRepository
) : BookAccessService {

    override fun canAccess(bookId: Long, authentication: Authentication): Boolean {
        val user = currentUser(authentication) ?: return false
        val book = bookRepository.findById(bookId).orElse(null) ?: return false
        val access = bookAccessRepository.findByUserAndBook(user, book).orElse(null) ?: return false
        return access.access == AccessLevel.ADMIN || access.access == AccessLevel.READ || access.access == AccessLevel.WRITE || access.access == AccessLevel.EXECUTE
    }

    fun resolveAccess(user: User, book: Book): AccessLevel? {
        return bookAccessRepository.findByUserAndBook(user, book).map { it.access }.orElse(null)
    }

    private fun currentUser(authentication: Authentication): User? {
        val email = authentication.name
        return userRepository.findByEmail(email).orElse(null)
    }
}
