-- schema.sql
-- Run this script before starting the application.
-- Compatible with H2, MySQL, and PostgreSQL (minor type differences noted).

-- Drop tables if they already exist (useful for re-running)
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;

-- Departments table
CREATE TABLE departments (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,   -- Use SERIAL in PostgreSQL
    name     VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

-- Employees table
CREATE TABLE employees (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    department_id BIGINT,
    salary        DECIMAL(10, 2),
    hire_date     DATE,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Seed data
INSERT INTO departments (name, location) VALUES ('Engineering', 'Bangalore');
INSERT INTO departments (name, location) VALUES ('Marketing',   'Mumbai');
INSERT INTO departments (name, location) VALUES ('Finance',     'Delhi');

INSERT INTO employees (name, email, department_id, salary, hire_date) VALUES
    ('John Doe',   'john@example.com',  1, 75000.00, '2022-03-15'),
    ('Jane Smith', 'jane@example.com',  1, 82000.00, '2021-06-01'),
    ('Bob Brown',  'bob@example.com',   2, 60000.00, '2023-01-20'),
    ('Alice Green','alice@example.com', 3, 70000.00, '2020-11-05');
