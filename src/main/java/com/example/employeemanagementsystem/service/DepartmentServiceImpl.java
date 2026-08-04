package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dao.DepartmentDAO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidInputException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDAO departmentDAO;

    public DepartmentServiceImpl(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @Override
    @Transactional
    public void saveDepartment(Department dept) {
        validateDepartment(dept);
        departmentDAO.save(dept);
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartment(int id) {
        Department dept = departmentDAO.findById(id);
        if (dept == null) {
            throw new DepartmentNotFoundException("Department not found with id: " + id);
        }
        return dept;
    }

    @Override
    @Transactional
    public void updateDepartment(Department dept) {
        validateDepartment(dept);
        Department existing = departmentDAO.findById(dept.getDeptId());
        if (existing == null) {
            throw new DepartmentNotFoundException("Department not found with id: " + dept.getDeptId());
        }
        existing.setDeptName(dept.getDeptName());
        existing.setLocation(dept.getLocation());
        departmentDAO.update(existing);
    }

    @Override
    @Transactional
    public void deleteDepartment(int id) {
        Department existing = departmentDAO.findById(id);
        if (existing == null) {
            throw new DepartmentNotFoundException("Department not found with id: " + id);
        }
        departmentDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentDAO.findAll();
    }

    private void validateDepartment(Department dept) {
        if (dept == null) {
            throw new InvalidInputException("Department payload cannot be null");
        }
        if (dept.getDeptName() == null || dept.getDeptName().trim().isEmpty()) {
            throw new InvalidInputException("Department name cannot be blank");
        }
        if (dept.getLocation() == null || dept.getLocation().trim().isEmpty()) {
            throw new InvalidInputException("Department location cannot be blank");
        }
    }
}
