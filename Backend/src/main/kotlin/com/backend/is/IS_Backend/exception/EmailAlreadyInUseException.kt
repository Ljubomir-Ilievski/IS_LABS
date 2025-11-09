package com.backend.`is`.IS_Backend.exception

/**
 * Thrown when attempting to register with an email that already exists.
 */
class EmailAlreadyInUseException(message: String = "Email is already in use") : RuntimeException(message)
