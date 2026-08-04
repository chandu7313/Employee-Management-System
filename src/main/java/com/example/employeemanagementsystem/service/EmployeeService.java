package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.entity.Employee;
import java.util.List;

/**
 * Service interface for Employee business operations.
 */
public interface EmployeeService {

    /**
     * Save a new employee after validating business rules.
     *
     * @param emp Employee entity to save
     */
    void saveEmployee(Employee emp);

    /**
     * Retrieve an employee by ID.
     *
     * @param id Employee ID
     * @return Employee entity
     */
    Employee getEmployee(int id);

    /**
     * Update an existing employee after validating business rules.
     *
     * @param emp Employee entity with updated data
     */
    void updateEmployee(Employee emp);

    /**
     * Delete an employee by ID.
     *
     * @param id Employee ID to delete
     */
    void deleteEmployee(int id);

    /**
     * Retrieve all employees.
     *
     * @return List of all Employee entities
     */
    List<Employee> getAllEmployees();
}
