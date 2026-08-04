package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({EmployeeDAOImpl.class, DepartmentDAOImpl.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class EmployeeDAOTest {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department savedDept;

    @BeforeEach
    void setUp() {
        Department dept = new Department("R&D", "Seattle");
        departmentDAO.save(dept);
        savedDept = dept;
    }

    @Test
    @DisplayName("EmployeeDAO - save, findById, update, delete, and findAll")
    void testEmployeeDAOCRUD() {
        // 1. Save
        Employee emp = new Employee("Robert Fox", "robert.fox@example.com", 80000.0, savedDept);
        employeeDAO.save(emp);
        assertTrue(emp.getEmpId() > 0, "Generated ID should be set after save");

        // 2. FindById
        Employee found = employeeDAO.findById(emp.getEmpId());
        assertNotNull(found);
        assertEquals("Robert Fox", found.getEmpName());
        assertEquals("robert.fox@example.com", found.getEmail());

        // 3. Update
        found.setSalary(88000.0);
        found.setEmpName("Robert F. Fox");
        employeeDAO.update(found);

        Employee updated = employeeDAO.findById(emp.getEmpId());
        assertEquals(88000.0, updated.getSalary());
        assertEquals("Robert F. Fox", updated.getEmpName());

        // 4. FindAll
        List<Employee> all = employeeDAO.findAll();
        assertFalse(all.isEmpty());

        // 5. Delete
        employeeDAO.delete(emp.getEmpId());
        Employee afterDelete = employeeDAO.findById(emp.getEmpId());
        assertNull(afterDelete, "Deleted employee should be null");
    }
}
