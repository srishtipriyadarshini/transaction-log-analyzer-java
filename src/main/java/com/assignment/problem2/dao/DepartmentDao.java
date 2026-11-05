package com.assignment.problem2.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.assignment.problem2.domain.Department;

/**
 * DAO interface for Department operations.
 */
public interface DepartmentDao {

    /** Inserts a new department. Returns the generated ID. */
    long create(String name, String location) throws SQLException;

    /** Finds a department by its primary key. Returns Optional.empty() if not found. */
    Optional<Department> findById(long id) throws SQLException;

    /** Returns all departments in the database. */
    List<Department> findAll() throws SQLException;
}
