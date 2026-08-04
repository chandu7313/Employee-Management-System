package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.GlobalExceptionHandler;
import com.example.employeemanagementsystem.service.DepartmentService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
@Import(GlobalExceptionHandler.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    private Department sampleDepartment;

    @BeforeEach
    void setUp() {
        sampleDepartment = new Department(1, "Engineering", "San Francisco");
    }

    @Test
    void testSaveDepartment() throws Exception {
        doNothing().when(departmentService).saveDepartment(any(Department.class));

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDepartment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deptName").value("Engineering"))
                .andExpect(jsonPath("$.location").value("San Francisco"));

        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    void testSaveDepartment_ValidationFailure() throws Exception {
        Department invalid = new Department(0, "", "");

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(departmentService, never()).saveDepartment(any(Department.class));
    }

    @Test
    void testGetDepartmentById_Success() throws Exception {
        when(departmentService.getDepartment(1)).thenReturn(sampleDepartment);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deptId").value(1))
                .andExpect(jsonPath("$.deptName").value("Engineering"));

        verify(departmentService, times(1)).getDepartment(1);
    }

    @Test
    void testGetDepartmentById_NotFound() throws Exception {
        when(departmentService.getDepartment(99)).thenThrow(new DepartmentNotFoundException("Department not found with id: 99"));

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Department not found with id: 99"));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        doNothing().when(departmentService).updateDepartment(any(Department.class));

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deptId").value(1));

        verify(departmentService, times(1)).updateDepartment(any(Department.class));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());

        verify(departmentService, times(1)).deleteDepartment(1);
    }

    @Test
    void testGetAllDepartments() throws Exception {
        List<Department> list = Arrays.asList(
                sampleDepartment,
                new Department(2, "Human Resources", "New York")
        );
        when(departmentService.getAllDepartments()).thenReturn(list);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].deptName").value("Engineering"))
                .andExpect(jsonPath("$[1].deptName").value("Human Resources"));
    }
}
