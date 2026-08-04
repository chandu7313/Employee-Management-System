package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        Department dept = new Department(1, "Engineering", "San Francisco");
        sampleEmployee = new Employee(1, "Alice Smith", "alice.smith@example.com", 90000.0, dept);
    }

    @Test
    @DisplayName("POST /api/employees - Should create employee and return 201 CREATED")
    void testSaveEmployee_Created() throws Exception {
        doNothing().when(employeeService).saveEmployee(any(Employee.class));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empName", is("Alice Smith")))
                .andExpect(jsonPath("$.email", is("alice.smith@example.com")))
                .andExpect(jsonPath("$.salary", is(90000.0)));

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    @DisplayName("POST /api/employees - Invalid payload should return 400 BAD REQUEST")
    void testSaveEmployee_ValidationError() throws Exception {
        Employee invalidEmp = new Employee(0, "", "not-an-email", -100.0);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmp)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors", notNullValue()));

        verify(employeeService, never()).saveEmployee(any(Employee.class));
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Should return employee and 200 OK")
    void testGetEmployee_Success() throws Exception {
        when(employeeService.getEmployee(1)).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId", is(1)))
                .andExpect(jsonPath("$.empName", is("Alice Smith")))
                .andExpect(jsonPath("$.email", is("alice.smith@example.com")));

        verify(employeeService, times(1)).getEmployee(1);
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Not found should return 404 NOT FOUND")
    void testGetEmployee_NotFound() throws Exception {
        when(employeeService.getEmployee(99)).thenThrow(new EmployeeNotFoundException(99));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Employee not found with id: 99")));

        verify(employeeService, times(1)).getEmployee(99);
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Should update employee and return 200 OK")
    void testUpdateEmployee_Success() throws Exception {
        doNothing().when(employeeService).updateEmployee(any(Employee.class));

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId", is(1)))
                .andExpect(jsonPath("$.empName", is("Alice Smith")));

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Should delete employee and return 204 NO CONTENT")
    void testDeleteEmployee_Success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1);
    }

    @Test
    @DisplayName("GET /api/employees - Should return all employees and 200 OK")
    void testGetAllEmployees_Success() throws Exception {
        List<Employee> list = Arrays.asList(
                sampleEmployee,
                new Employee(2, "Bob Jones", "bob@example.com", 75000.0)
        );
        when(employeeService.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].empName", is("Alice Smith")))
                .andExpect(jsonPath("$[1].empName", is("Bob Jones")));

        verify(employeeService, times(1)).getAllEmployees();
    }
}
