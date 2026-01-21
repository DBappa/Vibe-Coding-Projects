package org.example.windsurfmvc.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * Handles exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, ex);
        return handleExceptionInternal(
                ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(OrderValidationException.class)
    public ResponseEntity<Object> handleOrderValidationException(
            OrderValidationException ex, WebRequest request) {
        
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(
                ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException(
            InsufficientStockException ex, WebRequest request) {
        
        Map<String, Object> body = createErrorBody(HttpStatus.CONFLICT, ex);
        body.put("beerId", ex.getBeerId());
        body.put("requested", ex.getRequested());
        body.put("available", ex.getAvailable());
        
        return handleExceptionInternal(
                ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex);
        String errors = ex.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s", 
                        violation.getPropertyPath(), 
                        violation.getMessage()))
                .collect(Collectors.joining(", "));
        
        body.put("errors", errors);
        return handleExceptionInternal(
                ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {
        
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex);
        
        // Get all field errors
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        body.put("errors", errors);
        return handleExceptionInternal(
                ex, body, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        
        Map<String, Object> body = createErrorBody(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred");
        
        return handleExceptionInternal(
                ex, body, new HttpHeaders(), 
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private Map<String, Object> createErrorBody(HttpStatus status, Exception ex) {
        return createErrorBody(status, ex.getMessage());
    }
    
    private Map<String, Object> createErrorBody(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }
}
