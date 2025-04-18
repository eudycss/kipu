package com.seibe.docente.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String error, String message, Map<String, String> errors) {
        super(timestamp, status, error, message, null);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
} 