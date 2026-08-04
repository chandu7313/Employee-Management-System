package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of DepartmentDAO interface.
 * Wraps Spring Data JPA DepartmentRepository and EntityManager operations.
 */
@Repository
public class DepartmentDAOImpl implements DepartmentDAO {

    private final DepartmentRepository departmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DepartmentDAOImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void save(Department dept) {
        Department savedDept = departmentRepository.save(dept);
        if (dept != null && savedDept != null) {
            dept.setDeptId(savedDept.getDeptId());
        }
    }

    @Override
    public Department findById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public void update(Department dept) {
        departmentRepository.save(dept);
    }

    @Override
    public void delete(int id) {
        departmentRepository.deleteById(id);
    }
}
