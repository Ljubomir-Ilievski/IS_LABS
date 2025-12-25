package com.backend.`is`.IS_Backend.service.impl

import com.backend.`is`.IS_Backend.dto.book.BookAccessResultDto
import com.backend.`is`.IS_Backend.dto.book.BookDto
import com.backend.`is`.IS_Backend.enumerations.AccessLevel
import com.backend.`is`.IS_Backend.model.domain.Book
import com.backend.`is`.IS_Backend.repository.BookRepository
import com.backend.`is`.IS_Backend.repository.UserRepository
import com.backend.`is`.IS_Backend.service.intf.BookService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val bookAccessServiceImpl: BookAccessServiceImpl
) : BookService {

    override fun listAll(): List<BookDto> =
        bookRepository.findAll().map { it.toDto() }

    @PreAuthorize("@bookAccessService.canAccess(#bookId, authentication)")
    override fun ping(bookId: Long): BookAccessResultDto {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElseThrow()
        val book = bookRepository.findById(bookId).orElseThrow()

        val access = bookAccessServiceImpl.resolveAccess(user, book)

        val canRead = access == AccessLevel.READ || access == AccessLevel.ADMIN
        val canWrite = access == AccessLevel.WRITE || access == AccessLevel.ADMIN
        val canReadAndWrite = access == AccessLevel.ADMIN

        return BookAccessResultDto(
            bookId = book.id!!,
            canRead = canRead,
            canWrite = canWrite,
            canReadAndWrite = canReadAndWrite
        )
    }

    private fun Book.toDto() = BookDto(
        id = this.id!!,
        title = this.title,
        description = this.description
    )
}
