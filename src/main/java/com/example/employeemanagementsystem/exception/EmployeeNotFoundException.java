package com.example.employeemanagementsystem.exception;

/**
 * Custom exception thrown when an employee record is not found.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(int id) {
        super("Employee not found with id: " + id);
    }
}
