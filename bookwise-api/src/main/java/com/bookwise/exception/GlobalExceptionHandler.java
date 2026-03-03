package com.bookwise.exception;

import com.bookwise.web.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        Map<String, String> fields = null;

        if (ex instanceof MethodArgumentNotValidException manve) {
            fields = new LinkedHashMap<>();
            for (FieldError fe : manve.getBindingResult().getFieldErrors()) {
                fields.put(fe.getField(), fe.getDefaultMessage());
            }
        }

        String msg = ex.getMessage();
        // pt MissingServletRequestParameterException message-ul e cam lung; îl scurtăm puțin
        if (ex instanceof MissingServletRequestParameterException msrpe) {
            msg = "Missing required query parameter: " + msrpe.getParameterName();
        }

        return build(HttpStatus.BAD_REQUEST, msg, req, fields);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        // nu expunem stacktrace către client
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, null);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req, Map<String, String> fieldErrors) {
        ApiError body = new ApiError(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                null,
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}