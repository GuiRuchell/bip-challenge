package com.example.backend.exception;

public class BenefitConflictException extends RuntimeException {
    public BenefitConflictException(String message) {
        super(message);
    }
}
