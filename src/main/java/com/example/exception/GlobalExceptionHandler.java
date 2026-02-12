package com.example.exception;

import com.example.dto.Response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, status);
    }

    // 404 - Об'єкт не знайдено (БД)
    @ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 400 - Помилка валідації (наприклад, @Valid на DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Помилка валідації даних");
    }

    // 400 - Неправильні аргументи або формат дати
    @ExceptionHandler({IllegalArgumentException.class, DateTimeParseException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 403 - Доступ заборонено (Spring Security)
    @ExceptionHandler({AccessDeniedException.class, SecurityException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(Exception e) {
        return buildResponse(HttpStatus.FORBIDDEN, "У вас немає прав для цієї дії");
    }

    // 500 - Runtime і загальні помилки
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутрішня помилка сервера: " + e.getMessage());
    }
}