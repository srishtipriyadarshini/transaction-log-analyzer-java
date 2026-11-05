package com.assignment.problem2.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.assignment.problem2.dao.EmployeeDao;
import com.assignment.problem2.domain.Employee;
import com.assignment.problem2.exception.EmployeeNotFoundException;

/**
 * JDBC implementation of EmployeeDao.
 */
public class EmployeeDaoImpl implements EmployeeDao {

    private final DataSource dataSource;

    public EmployeeDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(String name, String email, long departmentId,
                       double salary, LocalDate hireDate) throws SQLException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email must not be blank");

        String sql = "INSERT INTO employees (name, email, department_id, salary, hire_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setLong(3, departmentId);
            ps.setDouble(4, salary);
            ps.setDate(5, Date.valueOf(hireDate));   // LocalDate → java.sql.Date
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new SQLException("No generated key returned.");
            }
        }
    }

    @Override
    public Optional<Employee> findById(long id) throws SQLException {
        String sql = "SELECT id, name, email, department_id, salary, hire_date FROM employees WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public void update(long id, String name, String email,
                       long departmentId, double salary) throws SQLException {
        String sql = "UPDATE employees SET name=?, email=?, department_id=?, salary=? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setLong(3, departmentId);
            ps.setDouble(4, salary);
            ps.setLong(5, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) throw new EmployeeNotFoundException(id);
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) throw new EmployeeNotFoundException(id);
        }
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT id, name, email, department_id, salary, hire_date FROM employees ORDER BY id";
        List<Employee> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) result.add(mapRow(rs));
        }
        return result;
    }

    @Override
    public List<Employee> findByDepartment(long departmentId) throws SQLException {
        String sql = "SELECT id, name, email, department_id, salary, hire_date FROM employees WHERE department_id = ?";
        List<Employee> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        }
        return result;
    }

    /** Maps a ResultSet row to an Employee. */
    private Employee mapRow(ResultSet rs) throws SQLException {
        Date sqlDate = rs.getDate("hire_date");
        LocalDate hireDate = sqlDate != null ? sqlDate.toLocalDate() : null;

        return new Employee(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getLong("department_id"),
                rs.getDouble("salary"),
                hireDate
        );
    }
}
