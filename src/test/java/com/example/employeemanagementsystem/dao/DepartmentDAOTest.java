package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({DepartmentDAOImpl.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class DepartmentDAOTest {

    @Autowired
    private DepartmentDAO departmentDAO;

    @Test
    @DisplayName("DepartmentDAO - save, findById, update, delete, and findAll")
    void testDepartmentDAOCRUD() {
        // 1. Save
        Department dept = new Department("Operations", "Denver");
        departmentDAO.save(dept);
        assertTrue(dept.getDeptId() > 0, "Generated ID should be assigned");

        // 2. FindById
        Department found = departmentDAO.findById(dept.getDeptId());
        assertNotNull(found);
        assertEquals("Operations", found.getDeptName());
        assertEquals("Denver", found.getLocation());

        // 3. Update
        found.setLocation("Boulder");
        found.setDeptName("Global Operations");
        departmentDAO.update(found);

        Department updated = departmentDAO.findById(dept.getDeptId());
        assertEquals("Boulder", updated.getLocation());
        assertEquals("Global Operations", updated.getDeptName());

        // 4. FindAll
        List<Department> list = departmentDAO.findAll();
        assertFalse(list.isEmpty());

        // 5. Delete
        departmentDAO.delete(dept.getDeptId());
        Department afterDelete = departmentDAO.findById(dept.getDeptId());
        assertNull(afterDelete, "Department should be null after deletion");
    }
}
