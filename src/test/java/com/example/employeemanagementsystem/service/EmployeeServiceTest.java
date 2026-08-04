package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dao.DepartmentDAO;
import com.example.employeemanagementsystem.dao.EmployeeDAO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        sampleEmployee = new Employee(1, "John Doe", "john.doe@example.com", 85000.0, sampleDepartment);
    }

    @Test
    @DisplayName("saveEmployee - Success with valid employee")
    void testSaveEmployee_Success() {
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);
        doAnswer(invocation -> {
            Employee emp = invocation.getArgument(0);
            emp.setEmpId(101);
            return null;
        }).when(employeeDAO).save(any(Employee.class));

        employeeService.saveEmployee(sampleEmployee);

        verify(departmentDAO, times(1)).findById(1);
        verify(employeeDAO, times(1)).save(sampleEmployee);
    }

    @Test
    @DisplayName("saveEmployee - Throws InvalidInputException when salary is zero or negative")
    void testSaveEmployee_InvalidSalary() {
        sampleEmployee.setSalary(-500.0);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            employeeService.saveEmployee(sampleEmployee);
        });

        assertTrue(exception.getMessage().contains("Salary must be greater than 0"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("saveEmployee - Throws InvalidInputException when email format is invalid")
    void testSaveEmployee_InvalidEmail() {
        sampleEmployee.setEmail("not-a-valid-email");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            employeeService.saveEmployee(sampleEmployee);
        });

        assertTrue(exception.getMessage().contains("Invalid email format"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("saveEmployee - Throws DepartmentNotFoundException when deptId does not exist")
    void testSaveEmployee_DepartmentNotFound() {
        when(departmentDAO.findById(1)).thenReturn(null);

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class, () -> {
            employeeService.saveEmployee(sampleEmployee);
        });

        assertTrue(exception.getMessage().contains("Department not found with id: 1"));
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("getEmployee - Success when employee exists")
    void testGetEmployee_Success() {
        when(employeeDAO.findById(1)).thenReturn(sampleEmployee);

        Employee result = employeeService.getEmployee(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getEmpName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(employeeDAO, times(1)).findById(1);
    }

    @Test
    @DisplayName("getEmployee - Throws EmployeeNotFoundException when employee does not exist")
    void testGetEmployee_NotFound() {
        when(employeeDAO.findById(99)).thenReturn(null);

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployee(99);
        });

        assertTrue(exception.getMessage().contains("Employee not found with id: 99"));
        verify(employeeDAO, times(1)).findById(99);
    }

    @Test
    @DisplayName("updateEmployee - Success when employee exists")
    void testUpdateEmployee_Success() {
        when(employeeDAO.findById(1)).thenReturn(sampleEmployee);
        when(departmentDAO.findById(1)).thenReturn(sampleDepartment);

        sampleEmployee.setSalary(95000.0);
        employeeService.updateEmployee(sampleEmployee);

        verify(employeeDAO, times(1)).findById(1);
        verify(employeeDAO, times(1)).update(sampleEmployee);
    }

    @Test
    @DisplayName("updateEmployee - Throws EmployeeNotFoundException when employee does not exist")
    void testUpdateEmployee_NotFound() {
        Employee nonExistent = new Employee(99, "Ghost", "ghost@example.com", 60000.0);
        when(employeeDAO.findById(99)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(nonExistent);
        });

        verify(employeeDAO, never()).update(any(Employee.class));
    }

    @Test
    @DisplayName("deleteEmployee - Success when employee exists")
    void testDeleteEmployee_Success() {
        when(employeeDAO.findById(1)).thenReturn(sampleEmployee);

        employeeService.deleteEmployee(1);

        verify(employeeDAO, times(1)).findById(1);
        verify(employeeDAO, times(1)).delete(1);
    }

    @Test
    @DisplayName("deleteEmployee - Throws EmployeeNotFoundException when employee does not exist")
    void testDeleteEmployee_NotFound() {
        when(employeeDAO.findById(99)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(99);
        });

        verify(employeeDAO, never()).delete(anyInt());
    }

    @Test
    @DisplayName("getAllEmployees - Returns list of employees")
    void testGetAllEmployees() {
        List<Employee> list = Arrays.asList(
                sampleEmployee,
                new Employee(2, "Jane Smith", "jane.smith@example.com", 90000.0, sampleDepartment)
        );
        when(employeeDAO.findAll()).thenReturn(list);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeDAO, times(1)).findAll();
    }
}
