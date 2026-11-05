package com.assignment.problem2.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.assignment.problem2.domain.Employee;

/**
 * DAO interface for Employee CRUD operations.
 */
public interface EmployeeDao {

    /** Creates a new employee. Returns the generated ID. */
    long create(String name, String email, long departmentId,
                double salary, LocalDate hireDate) throws SQLException;

    /** Finds an employee by ID. Returns Optional.empty() if not found. */
    Optional<Employee> findById(long id) throws SQLException;

    /** Updates an existing employee. Throws EmployeeNotFoundException if ID not found. */
    void update(long id, String name, String email,
                long departmentId, double salary) throws SQLException;

    /** Deletes an employee. Throws EmployeeNotFoundException if ID not found. */
    void delete(long id) throws SQLException;

    /** Returns all employees. */
    List<Employee> findAll() throws SQLException;

    /** Returns all employees in a given department. */
    List<Employee> findByDepartment(long departmentId) throws SQLException;
}
