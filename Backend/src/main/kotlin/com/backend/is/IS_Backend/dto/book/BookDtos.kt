package com.backend.`is`.IS_Backend.dto.book

data class BookDto(
    val id: Long,
    val title: String,
    val description: String
)

data class BookAccessResultDto(
    val bookId: Long,
    val canRead: Boolean,
    val canWrite: Boolean,
    val canReadAndWrite: Boolean
)
