package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Employee;
import java.util.List;

/**
 * Data Access Object (DAO) interface for Employee operations.
 */
public interface EmployeeDAO {

    /**
     * Save a new employee into the database.
     *
     * @param emp Employee entity to save
     */
    void save(Employee emp);

    /**
     * Find an employee by unique primary key ID.
     *
     * @param id Employee ID
     * @return Employee entity if found, null otherwise
     */
    Employee findById(int id);

    /**
     * Retrieve all employees from the database.
     *
     * @return List of all Employee entities
     */
    List<Employee> findAll();

    /**
     * Update an existing employee in the database.
     *
     * @param emp Employee entity containing updated data
     */
    void update(Employee emp);

    /**
     * Delete an employee by ID.
     *
     * @param id Employee ID to delete
     */
    void delete(int id);
}
