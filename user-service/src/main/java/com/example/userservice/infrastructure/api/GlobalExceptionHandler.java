package com.example.userservice.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> {
            Map<String, Object> err = new HashMap<>();
            err.put("field", error.getField());
            err.put("message", error.getDefaultMessage());
            err.put("rejectedValue", error.getRejectedValue());
            return err;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("type", "https://datatracker.ietf.org/doc/html/rfc9457");
        response.put("title", "Validation Failed");
        response.put("status", 400);
        response.put("detail", "Request validation failed");
        response.put("instance", "about:blank");
        response.put("timestamp", OffsetDateTime.now().toString());
        response.put("errors", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleBusinessConflict(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "https://datatracker.ietf.org/doc/html/rfc9457");
        response.put("title", "Business Conflict");
        response.put("status", 409);
        response.put("detail", ex.getMessage());
        response.put("instance", "about:blank");
        response.put("timestamp", OffsetDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
