package com.bookwise.exception;

import com.bookwise.web.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req, null, null);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
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
        if (ex instanceof MissingServletRequestParameterException msrpe) {
            msg = "Missing required query parameter: " + msrpe.getParameterName();
        }
        if (ex instanceof MissingRequestHeaderException mrhe) {
            msg = "Missing required header: " + mrhe.getHeaderName();
        }

        return build(HttpStatus.BAD_REQUEST, msg, req, fields, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        // trace id ca să poți corela client <-> logs
        String traceId = UUID.randomUUID().toString();

        log.error("Unhandled exception traceId={} {} {}", traceId, req.getMethod(), req.getRequestURI(), ex);

        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, null, traceId);
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest req,
            Map<String, String> fieldErrors,
            String traceId
    ) {
        ApiError body = new ApiError(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                traceId,
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}