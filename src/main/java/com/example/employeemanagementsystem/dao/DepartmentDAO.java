package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;

import java.util.List;

public interface DepartmentDAO {

    void save(Department dept);

    Department findById(int id);

    List<Department> findAll();

    void update(Department dept);

    void delete(int id);
}
