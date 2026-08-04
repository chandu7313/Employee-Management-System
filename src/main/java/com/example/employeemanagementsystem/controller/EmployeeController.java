package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Employee management endpoints under /api/employees.
 * Implements Controller -> Service -> DAO -> MySQL layered architecture.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Create a new employee.
     * POST /api/employees
     *
     * @param emp Employee payload to create
     * @return 201 CREATED with created Employee entity
     */
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee emp) {
        employeeService.saveEmployee(emp);
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    /**
     * Retrieve an employee by ID.
     * GET /api/employees/{id}
     *
     * @param id Employee ID
     * @return 200 OK with Employee entity, or 404 NOT FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
        Employee emp = employeeService.getEmployee(id);
        return ResponseEntity.ok(emp);
    }

    /**
     * Update an existing employee.
     * PUT /api/employees/{id}
     *
     * @param id  Employee ID to update
     * @param emp Updated Employee payload
     * @return 200 OK with updated Employee entity, or 404 NOT FOUND
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @Valid @RequestBody Employee emp) {
        emp.setEmpId(id);
        employeeService.updateEmployee(emp);
        return ResponseEntity.ok(emp);
    }

    /**
     * Delete an employee by ID.
     * DELETE /api/employees/{id}
     *
     * @param id Employee ID to delete
     * @return 204 NO CONTENT on success, or 404 NOT FOUND
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve all employees.
     * GET /api/employees
     *
     * @return 200 OK with list of all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
}
