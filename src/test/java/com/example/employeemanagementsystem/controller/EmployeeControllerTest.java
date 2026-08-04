package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.GlobalExceptionHandler;
import com.example.employeemanagementsystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee sampleEmployee;
    private Department sampleDepartment;

    @BeforeEach
    void setUp() {
        sampleDepartment = new Department(1, "Engineering", "San Francisco");
        sampleEmployee = new Employee(1, "Alice Johnson", "alice.johnson@example.com", 95000.0, sampleDepartment);
    }

    @Test
    void testSaveEmployee() throws Exception {
        doNothing().when(employeeService).saveEmployee(any(Employee.class));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empName").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice.johnson@example.com"))
                .andExpect(jsonPath("$.salary").value(95000.0));

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    void testSaveEmployee_InvalidData() throws Exception {
        Employee invalid = new Employee(0, "", "not-an-email", -500.0, null);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(employeeService, never()).saveEmployee(any(Employee.class));
    }

    @Test
    void testGetEmployeeById_Success() throws Exception {
        when(employeeService.getEmployee(1)).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value(1))
                .andExpect(jsonPath("$.empName").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice.johnson@example.com"));

        verify(employeeService, times(1)).getEmployee(1);
    }

    @Test
    void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployee(99)).thenThrow(new EmployeeNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 99"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        doNothing().when(employeeService).updateEmployee(any(Employee.class));

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value(1));

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> list = Arrays.asList(
                sampleEmployee,
                new Employee(2, "Bob Smith", "bob.smith@example.com", 85000.0, sampleDepartment)
        );
        when(employeeService.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].empName").value("Alice Johnson"))
                .andExpect(jsonPath("$[1].empName").value("Bob Smith"));
    }
}
