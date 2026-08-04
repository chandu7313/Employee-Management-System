package com.example.employeemanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int empId;

    @NotBlank(message = "Employee name is required")
    @Size(max = 100, message = "Employee name must not exceed 100 characters")
    @Column(name = "emp_name", nullable = false, length = 100)
    private String empName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than 0")
    @Column(name = "salary", nullable = false)
    private double salary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id")
    @JsonIgnoreProperties("employees")
    private Department department;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer deptId;

    public Employee() {
    }

    public Employee(String empName, String email, double salary) {
        this.empName = empName;
        this.email = email;
        this.salary = salary;
    }

    public Employee(String empName, String email, double salary, Department department) {
        this.empName = empName;
        this.email = email;
        this.salary = salary;
        this.department = department;
    }

    public Employee(int empId, String empName, String email, double salary, Department department) {
        this.empId = empId;
        this.empName = empName;
        this.email = email;
        this.salary = salary;
        this.department = department;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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

    public Integer getDeptId() {
        if (this.deptId != null) {
            return this.deptId;
        }
        if (this.department != null) {
            return this.department.getDeptId();
        }
        return null;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId &&
                Double.compare(employee.salary, salary) == 0 &&
                Objects.equals(empName, employee.empName) &&
                Objects.equals(email, employee.email);
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
                ", department=" + (department != null ? department.getDeptName() : null) +
                '}';
    }
}
