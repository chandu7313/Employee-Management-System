package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dao.DepartmentDAO;
import com.example.employeemanagementsystem.dao.EmployeeDAO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidInputException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final EmployeeDAO employeeDAO;
    private final DepartmentDAO departmentDAO;

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
        Employee employee = employeeDAO.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        return employee;
    }

    @Override
    @Transactional
    public void updateEmployee(Employee emp) {
        validateEmployee(emp);
        Employee existing = employeeDAO.findById(emp.getEmpId());
        if (existing == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + emp.getEmpId());
        }

        resolveDepartment(emp);

        existing.setEmpName(emp.getEmpName());
        existing.setEmail(emp.getEmail());
        existing.setSalary(emp.getSalary());
        if (emp.getDepartment() != null) {
            existing.setDepartment(emp.getDepartment());
        }
        employeeDAO.update(existing);
    }

    @Override
    @Transactional
    public void deleteEmployee(int id) {
        Employee existing = employeeDAO.findById(id);
        if (existing == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    private void validateEmployee(Employee emp) {
        if (emp == null) {
            throw new InvalidInputException("Employee payload cannot be null");
        }
        if (emp.getEmpName() == null || emp.getEmpName().trim().isEmpty()) {
            throw new InvalidInputException("Employee name cannot be blank");
        }
        if (emp.getEmail() == null || !EMAIL_PATTERN.matcher(emp.getEmail().trim()).matches()) {
            throw new InvalidInputException("Invalid email format: " + emp.getEmail());
        }
        if (emp.getSalary() <= 0) {
            throw new InvalidInputException("Salary must be greater than 0");
        }
    }

    private void resolveDepartment(Employee emp) {
        Integer targetDeptId = null;

        if (emp.getDeptId() != null && emp.getDeptId() > 0) {
            targetDeptId = emp.getDeptId();
        } else if (emp.getDepartment() != null && emp.getDepartment().getDeptId() > 0) {
            targetDeptId = emp.getDepartment().getDeptId();
        }

        if (targetDeptId != null) {
            Department dept = departmentDAO.findById(targetDeptId);
            if (dept == null) {
                throw new DepartmentNotFoundException("Department not found with id: " + targetDeptId);
            }
            emp.setDepartment(dept);
        }
    }
}
