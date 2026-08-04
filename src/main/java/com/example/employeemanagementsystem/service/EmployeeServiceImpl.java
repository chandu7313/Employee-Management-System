package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dao.DepartmentDAO;
import com.example.employeemanagementsystem.dao.EmployeeDAO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Service implementation for Employee business operations.
 * Implements business validation and transaction management, delegating persistence to EmployeeDAO.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final EmployeeDAO employeeDAO;
    private final DepartmentDAO departmentDAO;

    @Autowired
    public EmployeeServiceImpl(EmployeeDAO employeeDAO, DepartmentDAO departmentDAO) {
        this.employeeDAO = employeeDAO;
        this.departmentDAO = departmentDAO;
    }

    @Override
    @Transactional
    public void saveEmployee(Employee emp) {
        validateEmployee(emp);
        resolveDepartment(emp);
        employeeDAO.save(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployee(int id) {
        Employee emp = employeeDAO.findById(id);
        if (emp == null) {
            throw new EmployeeNotFoundException(id);
        }
        return emp;
    }

    @Override
    @Transactional
    public void updateEmployee(Employee emp) {
        if (emp == null) {
            throw new InvalidInputException("Employee data cannot be null");
        }
        // Verify employee exists before updating
        Employee existing = employeeDAO.findById(emp.getEmpId());
        if (existing == null) {
            throw new EmployeeNotFoundException(emp.getEmpId());
        }

        validateEmployee(emp);
        resolveDepartment(emp);
        employeeDAO.update(emp);
    }

    @Override
    @Transactional
    public void deleteEmployee(int id) {
        Employee existing = employeeDAO.findById(id);
        if (existing == null) {
            throw new EmployeeNotFoundException(id);
        }
        employeeDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    /**
     * Performs business rule validations on Employee data.
     */
    private void validateEmployee(Employee emp) {
        if (emp == null) {
            throw new InvalidInputException("Employee data cannot be null");
        }
        if (emp.getEmpName() == null || emp.getEmpName().trim().isEmpty()) {
            throw new InvalidInputException("Employee name cannot be blank");
        }
        if (emp.getEmail() == null || !EMAIL_PATTERN.matcher(emp.getEmail()).matches()) {
            throw new InvalidInputException("Invalid email format: " + emp.getEmail());
        }
        if (emp.getSalary() <= 0) {
            throw new InvalidInputException("Salary must be greater than 0. Provided: " + emp.getSalary());
        }
    }

    /**
     * Resolves and verifies Department association if provided.
     */
    private void resolveDepartment(Employee emp) {
        if (emp.getDepartment() != null && emp.getDepartment().getDeptId() > 0) {
            int deptId = emp.getDepartment().getDeptId();
            Department dept = departmentDAO.findById(deptId);
            if (dept == null) {
                throw new DepartmentNotFoundException(deptId);
            }
            emp.setDepartment(dept);
        }
    }
}
