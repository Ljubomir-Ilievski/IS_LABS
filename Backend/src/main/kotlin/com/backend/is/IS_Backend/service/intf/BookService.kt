package com.backend.`is`.IS_Backend.service.intf

import com.backend.`is`.IS_Backend.dto.book.BookAccessResultDto
import com.backend.`is`.IS_Backend.dto.book.BookDto

interface BookService {
    fun listAll(): List<BookDto>
    fun ping(bookId: Long): BookAccessResultDto
}
