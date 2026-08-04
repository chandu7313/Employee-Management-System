package com.example.employeemanagementsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EmployeeManagementSystemApplicationTests {

    @Test
    void contextLoads() {
    }
}
