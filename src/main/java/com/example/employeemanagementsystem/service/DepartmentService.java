package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.entity.Department;
import java.util.List;

/**
 * Service interface for Department business operations.
 */
public interface DepartmentService {

    /**
     * Save a new department.
     *
     * @param dept Department entity to save
     */
    void saveDepartment(Department dept);

    /**
     * Retrieve a department by ID.
     *
     * @param id Department ID
     * @return Department entity
     */
    Department getDepartment(int id);

    /**
     * Update an existing department.
     *
     * @param dept Department entity with updated data
     */
    void updateDepartment(Department dept);

    /**
     * Delete a department by ID.
     *
     * @param id Department ID to delete
     */
    void deleteDepartment(int id);

    /**
     * Retrieve all departments.
     *
     * @return List of all Department entities
     */
    List<Department> getAllDepartments();
}
