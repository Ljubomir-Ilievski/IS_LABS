package com.backend.`is`.IS_Backend.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.OffsetDateTime

/**
 * Standard error response returned by the API when an exception is handled.
 */
data class ApiError(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val timestamp: OffsetDateTime,
    val status: Int,
    val error: String,
    val message: String?,
    val path: String?
)
