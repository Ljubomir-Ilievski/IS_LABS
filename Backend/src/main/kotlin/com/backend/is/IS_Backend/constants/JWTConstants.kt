package com.backend.`is`.IS_Backend.constants

object JWTConstants {
    // IMPORTANT: Replace with a secure Base64-encoded 256-bit secret for HS256, or externalize via application.properties
    // Example to generate: new String(java.util.Base64.getEncoder().encode(java.security.SecureRandom().generateSeed(32)))
    const val SECRET_KEY: String = "wP5nUq+7m8HjI2o5YgR3sV9dK1aF4cE7pQ1rT6yZ8mXbC0dL2vN5qS3uV6wX9zA=="
    const val EXPIRATION_TIME: Long = 1000L * 60 * 60 * 24 // 24 hours
}