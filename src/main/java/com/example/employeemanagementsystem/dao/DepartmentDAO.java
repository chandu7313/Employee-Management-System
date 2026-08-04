package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;
import java.util.List;

/**
 * Data Access Object (DAO) interface for Department operations.
 */
public interface DepartmentDAO {

    /**
     * Save a new department into the database.
     *
     * @param dept Department entity to save
     */
    void save(Department dept);

    /**
     * Find a department by unique primary key ID.
     *
     * @param id Department ID
     * @return Department entity if found, null otherwise
     */
    Department findById(int id);

    /**
     * Retrieve all departments from the database.
     *
     * @return List of all Department entities
     */
    List<Department> findAll();

    /**
     * Update an existing department in the database.
     *
     * @param dept Department entity containing updated data
     */
    void update(Department dept);

    /**
     * Delete a department by ID.
     *
     * @param id Department ID to delete
     */
    void delete(int id);
}
