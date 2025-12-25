package com.backend.`is`.IS_Backend.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyInUseException::class)
    fun handleEmailAlreadyInUse(
        ex: EmailAlreadyInUseException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.CONFLICT
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(InvalidEmailFormatException::class)
    fun handleInvalidEmailFormat(
        ex: InvalidEmailFormatException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.BAD_REQUEST
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(WeakPasswordException::class)
    fun handleWeakPassword(
        ex: WeakPasswordException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.BAD_REQUEST
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(InvalidTimeWindowException::class)
    fun handleInvalidTimeWindow(
        ex: InvalidTimeWindowException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.BAD_REQUEST
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(
        ex: UserNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.NOT_FOUND
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(value = [ForbiddenOperationException::class, AdminElevationNotAllowedException::class])
    fun handleForbidden(
        ex: RuntimeException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.FORBIDDEN
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.BAD_REQUEST
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val body = ApiError(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity.status(status).body(body)
    }
}
