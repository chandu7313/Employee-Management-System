package com.example.employeemanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Entity representing an Employee.
 * Mapped to 'employees' table in MySQL.
 */
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int empId;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    @Column(name = "emp_name", nullable = false)
    private String empName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Positive(message = "Salary must be greater than 0")
    @Column(name = "salary", nullable = false)
    private double salary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id")
    @JsonIgnoreProperties("employees")
    private Department department;

    // Default Constructor
    public Employee() {
    }

    // Parameterized Constructor without ID
    public Employee(String empName, String email, double salary, Department department) {
        this.empName = empName;
        this.email = email;
        this.salary = salary;
        this.department = department;
    }

    // Parameterized Constructor with all fields
    public Employee(int empId, String empName, String email, double salary, Department department) {
        this.empId = empId;
        this.empName = empName;
        this.email = email;
        this.salary = salary;
        this.department = department;
    }

    // Parameterized Constructor with primitive fields
    public Employee(int empId, String empName, String email, double salary) {
        this.empId = empId;
        this.empName = empName;
        this.email = email;
        this.salary = salary;
    }

    // Getters and Setters
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    // Alias id getter/setter for compatibility
    public int getId() {
        return empId;
    }

    public void setId(int id) {
        this.empId = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    // Alias name getter/setter for compatibility
    public String getName() {
        return empName;
    }

    public void setName(String name) {
        this.empName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Helper to read deptId directly from associated Department if present.
     */
    @JsonGetter("deptId")
    public Integer getDeptId() {
        return (department != null) ? department.getDeptId() : null;
    }

    /**
     * Helper to allow setting deptId directly from JSON payloads.
     */
    @JsonSetter("deptId")
    public void setDeptId(Integer deptId) {
        if (deptId != null && deptId > 0) {
            if (this.department == null) {
                this.department = new Department();
            }
            this.department.setDeptId(deptId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId && Double.compare(employee.salary, salary) == 0 &&
                Objects.equals(empName, employee.empName) && Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, empName, email, salary);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", department=" + (department != null ? department.getDeptName() : "null") +
                '}';
    }
}
