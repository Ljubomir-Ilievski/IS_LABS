package com.backend.`is`.IS_Backend.exception

/**
 * Thrown when a provided email does not match the expected format.
 */
class InvalidEmailFormatException(message: String = "Invalid email format") : RuntimeException(message)
