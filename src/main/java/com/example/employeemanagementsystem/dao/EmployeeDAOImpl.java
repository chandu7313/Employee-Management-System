package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EmployeeDAOImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void save(Employee emp) {
        Employee saved = employeeRepository.save(emp);
        if (emp != null && saved != null) {
            emp.setEmpId(saved.getEmpId());
        }
    }

    @Override
    public Employee findById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void update(Employee emp) {
        employeeRepository.save(emp);
    }

    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }
}
