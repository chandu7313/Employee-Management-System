package com.example.employeemanagementsystem.exception;

/**
 * Custom exception thrown when business logic validation fails on input data.
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}
