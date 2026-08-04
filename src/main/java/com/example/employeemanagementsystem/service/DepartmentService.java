package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.entity.Department;

import java.util.List;

public interface DepartmentService {

    void saveDepartment(Department dept);

    Department getDepartment(int id);

    void updateDepartment(Department dept);

    void deleteDepartment(int id);

    List<Department> getAllDepartments();
}
