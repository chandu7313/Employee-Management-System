package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.service.DepartmentService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Department sampleDepartment;

    @BeforeEach
    void setUp() {
        sampleDepartment = new Department(1, "Finance", "Chicago");
    }

    @Test
    @DisplayName("POST /api/departments - Should create department and return 201 CREATED")
    void testSaveDepartment_Created() throws Exception {
        doNothing().when(departmentService).saveDepartment(any(Department.class));

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDepartment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deptName", is("Finance")))
                .andExpect(jsonPath("$.location", is("Chicago")));

        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    @DisplayName("POST /api/departments - Invalid payload should return 400 BAD REQUEST")
    void testSaveDepartment_ValidationError() throws Exception {
        Department invalidDept = new Department(0, "", "");

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDept)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors", notNullValue()));

        verify(departmentService, never()).saveDepartment(any(Department.class));
    }

    @Test
    @DisplayName("GET /api/departments/{id} - Should return department and 200 OK")
    void testGetDepartment_Success() throws Exception {
        when(departmentService.getDepartment(1)).thenReturn(sampleDepartment);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deptId", is(1)))
                .andExpect(jsonPath("$.deptName", is("Finance")))
                .andExpect(jsonPath("$.location", is("Chicago")));

        verify(departmentService, times(1)).getDepartment(1);
    }

    @Test
    @DisplayName("GET /api/departments/{id} - Not found should return 404 NOT FOUND")
    void testGetDepartment_NotFound() throws Exception {
        when(departmentService.getDepartment(99)).thenThrow(new DepartmentNotFoundException(99));

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Department not found with id: 99")));

        verify(departmentService, times(1)).getDepartment(99);
    }

    @Test
    @DisplayName("PUT /api/departments/{id} - Should update department and return 200 OK")
    void testUpdateDepartment_Success() throws Exception {
        doNothing().when(departmentService).updateDepartment(any(Department.class));

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deptId", is(1)))
                .andExpect(jsonPath("$.deptName", is("Finance")));

        verify(departmentService, times(1)).updateDepartment(any(Department.class));
    }

    @Test
    @DisplayName("DELETE /api/departments/{id} - Should delete department and return 204 NO CONTENT")
    void testDeleteDepartment_Success() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).deleteDepartment(1);
    }

    @Test
    @DisplayName("GET /api/departments - Should return all departments and 200 OK")
    void testGetAllDepartments_Success() throws Exception {
        List<Department> list = Arrays.asList(
                sampleDepartment,
                new Department(2, "Human Resources", "New York")
        );
        when(departmentService.getAllDepartments()).thenReturn(list);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].deptName", is("Finance")))
                .andExpect(jsonPath("$[1].deptName", is("Human Resources")));

        verify(departmentService, times(1)).getAllDepartments();
    }
}
