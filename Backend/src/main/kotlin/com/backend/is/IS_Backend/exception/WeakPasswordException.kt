package com.backend.`is`.IS_Backend.exception

/**
 * Thrown when a provided password does not meet the strength requirements.
 */
class WeakPasswordException(message: String = "Password does not meet strength requirements") : RuntimeException(message)
