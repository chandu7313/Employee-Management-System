package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    /**
     * Find a department by name.
     */
    Optional<Department> findByDeptName(String deptName);

    /**
     * Check if a department exists with given name.
     */
    boolean existsByDeptName(String deptName);
}
