package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.service.DepartmentService;
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
 * REST Controller for Department management endpoints under /api/departments.
 * Implements Controller -> Service -> DAO -> MySQL layered architecture.
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Create a new department.
     * POST /api/departments
     *
     * @param dept Department payload to create
     * @return 201 CREATED with created Department entity
     */
    @PostMapping
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody Department dept) {
        departmentService.saveDepartment(dept);
        return new ResponseEntity<>(dept, HttpStatus.CREATED);
    }

    /**
     * Retrieve a department by ID.
     * GET /api/departments/{id}
     *
     * @param id Department ID
     * @return 200 OK with Department entity, or 404 NOT FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable int id) {
        Department dept = departmentService.getDepartment(id);
        return ResponseEntity.ok(dept);
    }

    /**
     * Update an existing department.
     * PUT /api/departments/{id}
     *
     * @param id   Department ID to update
     * @param dept Updated Department payload
     * @return 200 OK with updated Department entity, or 404 NOT FOUND
     */
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @Valid @RequestBody Department dept) {
        dept.setDeptId(id);
        departmentService.updateDepartment(dept);
        return ResponseEntity.ok(dept);
    }

    /**
     * Delete a department by ID.
     * DELETE /api/departments/{id}
     *
     * @param id Department ID to delete
     * @return 204 NO CONTENT on success, or 404 NOT FOUND
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve all departments.
     * GET /api/departments
     *
     * @return 200 OK with list of all departments
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
}
