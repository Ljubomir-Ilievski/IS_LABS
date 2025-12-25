package com.backend.`is`.IS_Backend.exception

class UserNotFoundException(message: String = "User with provided email not found") : RuntimeException(message)
