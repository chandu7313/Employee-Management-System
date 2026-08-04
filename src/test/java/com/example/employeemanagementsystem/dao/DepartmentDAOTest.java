package com.example.employeemanagementsystem.dao;

import com.example.employeemanagementsystem.entity.Department;
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
    void testDepartmentDAOCRUD() {
        Department dept = new Department("Operations", "Denver");
        departmentDAO.save(dept);
        assertTrue(dept.getDeptId() > 0);

        Department found = departmentDAO.findById(dept.getDeptId());
        assertNotNull(found);
        assertEquals("Operations", found.getDeptName());
        assertEquals("Denver", found.getLocation());

        found.setLocation("Boulder");
        found.setDeptName("Global Operations");
        departmentDAO.update(found);

        Department updated = departmentDAO.findById(dept.getDeptId());
        assertEquals("Boulder", updated.getLocation());
        assertEquals("Global Operations", updated.getDeptName());

        List<Department> list = departmentDAO.findAll();
        assertFalse(list.isEmpty());

        departmentDAO.delete(dept.getDeptId());
        Department afterDelete = departmentDAO.findById(dept.getDeptId());
        assertNull(afterDelete);
    }
}
