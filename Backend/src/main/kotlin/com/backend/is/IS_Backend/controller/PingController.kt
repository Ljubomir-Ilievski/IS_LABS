package com.backend.`is`.IS_Backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ping")
class PingController {

    @GetMapping("/reader")
    fun readerPing(): ResponseEntity<String> = ResponseEntity.ok("reader-ok")

    @GetMapping("/librarian")
    fun librarianPing(): ResponseEntity<String> = ResponseEntity.ok("librarian-ok")

    @GetMapping("/admin")
    fun adminPing(): ResponseEntity<String> = ResponseEntity.ok("admin-ok")
}
