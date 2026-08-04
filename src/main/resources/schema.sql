-- =================================================================
-- MySQL 8 Database Schema: employee_management_db
-- =================================================================

CREATE DATABASE IF NOT EXISTS employee_management_db;
USE employee_management_db;

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS departments (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Employees Table
CREATE TABLE IF NOT EXISTS employees (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    salary DOUBLE NOT NULL,
    dept_id INT,
    CONSTRAINT fk_employee_department
        FOREIGN KEY (dept_id)
        REFERENCES departments (dept_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =================================================================
-- Sample Seed Data
-- =================================================================

INSERT INTO departments (dept_name, location) VALUES
('Engineering', 'San Francisco, CA'),
('Human Resources', 'New York, NY'),
('Marketing', 'Austin, TX');

INSERT INTO employees (emp_name, email, salary, dept_id) VALUES
('Alice Johnson', 'alice.johnson@example.com', 95000.00, 1),
('Bob Smith', 'bob.smith@example.com', 82000.00, 1),
('Carol Williams', 'carol.williams@example.com', 68000.00, 2),
('David Brown', 'david.brown@example.com', 72000.00, 3);
