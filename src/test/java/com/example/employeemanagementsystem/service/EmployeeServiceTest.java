package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dao.DepartmentDAO;
import com.example.employeemanagementsystem.dao.EmployeeDAO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
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
class EmployeeServiceTest {

    @Mock
    private EmployeeDAO employeeDAO;

    @Mock
    private DepartmentDAO departmentDAO;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department sampleDepartment;
    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleDepartment = new Department(1, "Engineering", "San Francisco");
        sampleEmployee = new Employee(1, "John Doe", "john.doe@example.com", 75000.0, sampleDepartment);
    }

    @Test
    void testSaveEmployee_Success() {
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);
        doNothing().when(employeeDAO).save(any(Employee.class));

        Employee newEmp = new Employee("Jane Doe", "jane.doe@example.com", 80000.0, sampleDepartment);
        employeeService.saveEmployee(newEmp);

        verify(departmentDAO, times(1)).findById(1);
        verify(employeeDAO, times(1)).save(newEmp);
    }

    @Test
    void testSaveEmployee_BlankName_ThrowsException() {
        Employee invalidEmp = new Employee("  ", "valid@example.com", 50000.0, sampleDepartment);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            employeeService.saveEmployee(invalidEmp);
        });

        assertTrue(exception.getMessage().contains("Employee name cannot be blank"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    void testSaveEmployee_InvalidEmail_ThrowsException() {
        Employee invalidEmp = new Employee("John", "invalid-email-format", 50000.0, sampleDepartment);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            employeeService.saveEmployee(invalidEmp);
        });

        assertTrue(exception.getMessage().contains("Invalid email format"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    void testSaveEmployee_NegativeSalary_ThrowsException() {
        Employee invalidEmp = new Employee("John", "john@example.com", -100.0, sampleDepartment);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            employeeService.saveEmployee(invalidEmp);
        });

        assertTrue(exception.getMessage().contains("Salary must be greater than 0"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    void testSaveEmployee_NonExistentDepartment_ThrowsException() {
        Department nonExistentDept = new Department(999, "Unknown", "Nowhere");
        Employee empWithBadDept = new Employee("John", "john@example.com", 60000.0, nonExistentDept);

        when(departmentDAO.findById(999)).thenReturn(null);

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class, () -> {
            employeeService.saveEmployee(empWithBadDept);
        });

        assertTrue(exception.getMessage().contains("Department not found with id: 999"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    void testGetEmployee_Success() {
        when(employeeDAO.findById(1)).thenReturn(sampleEmployee);

        Employee result = employeeService.getEmployee(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getEmpName());
        verify(employeeDAO, times(1)).findById(1);
    }

    @Test
    void testGetEmployee_NotFound_ThrowsException() {
        when(employeeDAO.findById(99)).thenReturn(null);

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployee(99);
        });

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(employeeDAO, times(1)).findById(99);
    }

    @Test
    void testUpdateEmployee_Success() {
        when(employeeDAO.findById(1)).thenReturn(sampleEmployee);
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);

        sampleEmployee.setSalary(90000.0);
        employeeService.updateEmployee(sampleEmployee);

        verify(employeeDAO, times(1)).findById(1);
        verify(employeeDAO, times(1)).update(sampleEmployee);
    }

    @Test
    void testUpdateEmployee_NotFound_ThrowsException() {
        Employee nonExistent = new Employee(99, "Ghost", "ghost@example.com", 50000.0, null);
        when(employeeDAO.findById(99)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(nonExistent);
        });

        verify(employeeDAO, never()).update(any(Employee.class));
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeDAO.findById(1)).thenReturn(sampleEmployee);

        employeeService.deleteEmployee(1);

        verify(employeeDAO, times(1)).findById(1);
        verify(employeeDAO, times(1)).delete(1);
    }

    @Test
    void testDeleteEmployee_NotFound_ThrowsException() {
        when(employeeDAO.findById(99)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(99);
        });

        verify(employeeDAO, never()).delete(anyInt());
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> list = Arrays.asList(
                sampleEmployee,
                new Employee(2, "Alice Smith", "alice@example.com", 85000.0, sampleDepartment)
        );
        when(employeeDAO.findAll()).thenReturn(list);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeDAO, times(1)).findAll();
    }
}
