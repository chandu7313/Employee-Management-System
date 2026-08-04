package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
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

    private Department savedDept;

    @BeforeEach
    void setUp() {
        Department dept = new Department("R&D", "Seattle");
        departmentDAO.save(dept);
        savedDept = dept;
    }

    @Test
    void testEmployeeDAOCRUD() {
        Employee emp = new Employee("Robert Fox", "robert.fox@example.com", 80000.0, savedDept);
        employeeDAO.save(emp);
        assertTrue(emp.getEmpId() > 0);

        Employee found = employeeDAO.findById(emp.getEmpId());
        assertNotNull(found);
        assertEquals("Robert Fox", found.getEmpName());
        assertEquals("robert.fox@example.com", found.getEmail());

        found.setSalary(88000.0);
        found.setEmpName("Robert F. Fox");
        employeeDAO.update(found);

        Employee updated = employeeDAO.findById(emp.getEmpId());
        assertEquals(88000.0, updated.getSalary());
        assertEquals("Robert F. Fox", updated.getEmpName());

        List<Employee> all = employeeDAO.findAll();
        assertFalse(all.isEmpty());

        employeeDAO.delete(emp.getEmpId());
        Employee afterDelete = employeeDAO.findById(emp.getEmpId());
        assertNull(afterDelete);
    }
}
