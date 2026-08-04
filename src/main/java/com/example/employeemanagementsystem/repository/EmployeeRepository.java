package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Find an employee by unique email address.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Check if an employee exists with given email.
     */
    boolean existsByEmail(String email);
}
