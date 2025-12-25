package com.backend.`is`.IS_Backend.data

import com.backend.`is`.IS_Backend.model.domain.Admin
import com.backend.`is`.IS_Backend.model.domain.Librarian
import com.backend.`is`.IS_Backend.model.domain.Reader
import com.backend.`is`.IS_Backend.model.domain.Book
import com.backend.`is`.IS_Backend.model.domain.BookAccess
import com.backend.`is`.IS_Backend.enumerations.AccessLevel
import com.backend.`is`.IS_Backend.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val adminRepository: AdminRepository,
    private val librarianRepository: LibrarianRepository,
    private val readerRepository: ReaderRepository,
    private val bookRepository: BookRepository,
    private val bookAccessRepository: BookAccessRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        seedAdmin()
        seedLibrarian()
        seedReader()
        seedBooksAndAccess()
    }

    private fun seedAdmin() {
        val email = "ilievski.ljubomir@gmail.com"
        if (userRepository.findByEmail(email).isEmpty) {
            val admin = Admin(
                email = email,
                password = passwordEncoder.encode(DEFAULT_PASSWORD)
            )
            adminRepository.save(admin)
        }
    }

    private fun seedLibrarian() {
        val email = "librarian@seed.local"
        if (userRepository.findByEmail(email).isEmpty) {
            val librarian = Librarian(
                email = email,
                password = passwordEncoder.encode(DEFAULT_PASSWORD)
            )
            librarianRepository.save(librarian)
        }
    }

    private fun seedReader() {
        val email = "reader@seed.local"
        if (userRepository.findByEmail(email).isEmpty) {
            val reader = Reader(
                email = email,
                password = passwordEncoder.encode(DEFAULT_PASSWORD)
            )
            readerRepository.save(reader)
        }
    }

    private fun seedBooksAndAccess() {
        if (bookRepository.count() == 0L) {
            val book1 = bookRepository.save(Book(title = "Clean Code", description = "A Handbook of Agile Software Craftsmanship"))
            val book2 = bookRepository.save(Book(title = "Effective Kotlin", description = "Best practices for Kotlin"))
            val book3 = bookRepository.save(Book(title = "Spring in Action", description = "Comprehensive Spring guide"))

            val admin = userRepository.findByEmail("ilievski.ljubomir@gmail.com").orElse(null)
            val librarian = userRepository.findByEmail("librarian@seed.local").orElse(null)
            val reader = userRepository.findByEmail("reader@seed.local").orElse(null)

            if (admin != null) {
                bookAccessRepository.save(BookAccess(user = admin, book = book1, access = AccessLevel.ADMIN))
                bookAccessRepository.save(BookAccess(user = admin, book = book2, access = AccessLevel.ADMIN))
                bookAccessRepository.save(BookAccess(user = admin, book = book3, access = AccessLevel.ADMIN))
            }
            if (librarian != null) {
                bookAccessRepository.save(BookAccess(user = librarian, book = book2, access = AccessLevel.WRITE))
            }
            if (reader != null) {
                bookAccessRepository.save(BookAccess(user = reader, book = book1, access = AccessLevel.READ))
                bookAccessRepository.save(BookAccess(user = reader, book = book3, access = AccessLevel.EXECUTE))
            }
        }
    }

    companion object {
        private const val DEFAULT_PASSWORD = "admin"
    }
}
