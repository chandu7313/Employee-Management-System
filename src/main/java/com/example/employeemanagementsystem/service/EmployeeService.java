package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.entity.Employee;

import java.util.List;

public interface EmployeeService {

    void saveEmployee(Employee emp);

    Employee getEmployee(int id);

    void updateEmployee(Employee emp);

    void deleteEmployee(int id);

    List<Employee> getAllEmployees();
}
