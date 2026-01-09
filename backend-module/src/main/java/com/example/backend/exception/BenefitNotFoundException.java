package com.example.backend.exception;

public class BenefitNotFoundException extends RuntimeException {
    public BenefitNotFoundException(String message) {
        super(message);
    }
}
