package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dao.DepartmentDAO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentDAO departmentDAO;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department sampleDepartment;

    @BeforeEach
    void setUp() {
        sampleDepartment = new Department(1, "Finance", "Chicago");
    }

    @Test
    void testSaveDepartment_Success() {
        doNothing().when(departmentDAO).save(any(Department.class));

        departmentService.saveDepartment(sampleDepartment);

        verify(departmentDAO, times(1)).save(sampleDepartment);
    }

    @Test
    void testSaveDepartment_BlankName() {
        Department invalid = new Department(0, "  ", "Chicago");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            departmentService.saveDepartment(invalid);
        });

        assertTrue(exception.getMessage().contains("Department name cannot be blank"));
        verify(departmentDAO, never()).save(any(Department.class));
    }

    @Test
    void testSaveDepartment_BlankLocation() {
        Department invalid = new Department(0, "Finance", "  ");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            departmentService.saveDepartment(invalid);
        });

        assertTrue(exception.getMessage().contains("Department location cannot be blank"));
        verify(departmentDAO, never()).save(any(Department.class));
    }

    @Test
    void testGetDepartment_Success() {
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);

        Department result = departmentService.getDepartment(1);

        assertNotNull(result);
        assertEquals("Finance", result.getDeptName());
        verify(departmentDAO, times(1)).findById(1);
    }

    @Test
    void testGetDepartment_NotFound() {
        when(departmentDAO.findById(99)).thenReturn(null);

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class, () -> {
            departmentService.getDepartment(99);
        });

        assertTrue(exception.getMessage().contains("Department not found with id: 99"));
        verify(departmentDAO, times(1)).findById(99);
    }

    @Test
    void testUpdateDepartment_Success() {
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);

        sampleDepartment.setDeptName("Global Finance");
        departmentService.updateDepartment(sampleDepartment);

        verify(departmentDAO, times(1)).findById(1);
        verify(departmentDAO, times(1)).update(sampleDepartment);
    }

    @Test
    void testUpdateDepartment_NotFound() {
        Department nonExistent = new Department(99, "Unknown", "Unknown");
        when(departmentDAO.findById(99)).thenReturn(null);

        assertThrows(DepartmentNotFoundException.class, () -> {
            departmentService.updateDepartment(nonExistent);
        });

        verify(departmentDAO, never()).update(any(Department.class));
    }

    @Test
    void testDeleteDepartment_Success() {
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);

        departmentService.deleteDepartment(1);

        verify(departmentDAO, times(1)).findById(1);
        verify(departmentDAO, times(1)).delete(1);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        when(departmentDAO.findById(99)).thenReturn(null);

        assertThrows(DepartmentNotFoundException.class, () -> {
            departmentService.deleteDepartment(99);
        });

        verify(departmentDAO, never()).delete(anyInt());
    }

    @Test
    void testGetAllDepartments() {
        List<Department> list = Arrays.asList(
                sampleDepartment,
                new Department(2, "Human Resources", "New York")
        );
        when(departmentDAO.findAll()).thenReturn(list);

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        verify(departmentDAO, times(1)).findAll();
    }
}
