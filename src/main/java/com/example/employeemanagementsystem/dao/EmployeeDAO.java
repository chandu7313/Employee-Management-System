package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    void save(Employee emp);

    Employee findById(int id);

    List<Employee> findAll();

    void update(Employee emp);

    void delete(int id);
}
