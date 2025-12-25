package com.backend.`is`.IS_Backend.exception

class InvalidTimeWindowException(message: String = "Invalid time window: 'from' must be before 'to' and 'to' must be in the future") : RuntimeException(message)
