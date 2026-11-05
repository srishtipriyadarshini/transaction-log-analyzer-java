package com.assignment.problem2.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.assignment.problem2.dao.DepartmentDao;
import com.assignment.problem2.domain.Department;

/**
 * JDBC implementation of DepartmentDao.
 */
public class DepartmentDaoImpl implements DepartmentDao {

    private final DataSource dataSource;

    public DepartmentDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(String name, String location) throws SQLException {
        // Basic validation
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Department name must not be blank");

        String sql = "INSERT INTO departments (name, location) VALUES (?, ?)";

        // try-with-resources: Connection and PreparedStatement auto-closed
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, location);
            ps.executeUpdate();

            // Retrieve the auto-generated primary key
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("No generated key returned after department insert.");
            }
        }
    }

    @Override
    public Optional<Department> findById(long id) throws SQLException {
        String sql = "SELECT id, name, location FROM departments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);  // safe parameterised binding

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty(); // not found
            }
        }
    }

    @Override
    public List<Department> findAll() throws SQLException {
        String sql = "SELECT id, name, location FROM departments ORDER BY id";
        List<Department> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    /** Maps a ResultSet row to a Department object. */
    private Department mapRow(ResultSet rs) throws SQLException {
        return new Department(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("location")
        );
    }
}
