package com.example.employeemanagementsystem.exception;

/**
 * Custom exception thrown when a department record is not found.
 */
public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(int id) {
        super("Department not found with id: " + id);
    }
}
