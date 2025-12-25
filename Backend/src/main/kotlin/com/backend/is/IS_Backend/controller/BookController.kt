package com.backend.`is`.IS_Backend.controller

import com.backend.`is`.IS_Backend.dto.book.BookAccessResultDto
import com.backend.`is`.IS_Backend.dto.book.BookDto
import com.backend.`is`.IS_Backend.service.intf.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping
    fun listAll(): List<BookDto> = bookService.listAll()

    @PostMapping("/{id}/ping")
    fun ping(@PathVariable("id") id: Long): ResponseEntity<BookAccessResultDto> {
        val result = bookService.ping(id)
        return ResponseEntity.ok(result)
    }
}
