package com.assignment.problem3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.assignment.problem2.dao.DepartmentDao;
import com.assignment.problem2.dao.EmployeeDao;
import com.assignment.problem2.domain.Department;
import com.assignment.problem2.domain.Employee;

/**
 * Generates reports by:
 *  1. Reading config (from Problem 3 - ReportConfig)
 *  2. Calling DAOs from Problem 2 to fetch data
 *  3. Streaming results and writing to a CSV file
 */
public class ReportGenerator {

    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;

    public ReportGenerator(EmployeeDao employeeDao, DepartmentDao departmentDao) {
        this.employeeDao   = employeeDao;
        this.departmentDao = departmentDao;
    }

    /**
     * Generates a report based on the given config file path.
     * Reads config → queries DB via DAOs → writes CSV output file.
     */
    public void generate(InputStream configFilePath) throws IOException, SQLException {
        // Step 1: Load config
        ReportConfig config = new ReportConfig(configFilePath);
        System.out.println("Generating report with config: " + config);

        // Step 2: Dispatch to correct report type
        switch (config.getReportType()) {
            case "EMPLOYEES_BY_DEPARTMENT":
                generateEmployeesByDepartment(config);
                break;
            case "ALL_EMPLOYEES":
                generateAllEmployees(config);
                break;
            default:
                throw new IllegalArgumentException("Unknown report type: " + config.getReportType());
        }
    }

    // -------------------------------------------------------------------------
    // Report: Employees filtered by department
    // -------------------------------------------------------------------------
    private void generateEmployeesByDepartment(ReportConfig config) throws IOException, SQLException {
        long deptId = config.getDepartmentId();

        // Fetch department name for the report
        Optional<Department> dept = departmentDao.findById(deptId);
        String deptName = dept.map(Department::getName).orElse("Unknown");

        // Fetch employees via DAO (reusing Problem 2's layer)
        List<Employee> employees = employeeDao.findByDepartment(deptId);

        // Transform employee list → CSV lines using streams
        List<String> lines = employees.stream()
                .map(emp -> String.join(",",
                        String.valueOf(emp.getId()),
                        emp.getName(),
                        emp.getEmail(),
                        deptName,
                        String.format("%.2f", emp.getSalary()),
                        String.valueOf(emp.getHireDate())
                ))
                .collect(Collectors.toList());

        // Write to output file using BufferedWriter + try-with-resources
        writeReport(
                config.getOutputFile(),
                "employeeId,name,email,departmentName,salary,hireDate",
                lines
        );
    }

    // -------------------------------------------------------------------------
    // Report: All employees (no department filter)
    // -------------------------------------------------------------------------
    private void generateAllEmployees(ReportConfig config) throws IOException, SQLException {
        List<Employee> employees = employeeDao.findAll();

        List<String> lines = employees.stream()
                .map(emp -> String.join(",",
                        String.valueOf(emp.getId()),
                        emp.getName(),
                        emp.getEmail(),
                        String.valueOf(emp.getDepartmentId()),
                        String.format("%.2f", emp.getSalary()),
                        String.valueOf(emp.getHireDate())
                ))
                .collect(Collectors.toList());

        writeReport(
                config.getOutputFile(),
                "employeeId,name,email,departmentId,salary,hireDate",
                lines
        );
    }

    // -------------------------------------------------------------------------
    // Shared file-writing helper
    // -------------------------------------------------------------------------

    /**
     * Writes a header + data lines to a CSV file.
     */
    private void writeReport(String outputPath, String header, List<String> lines)
            throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            // Write header
            writer.write(header);
            writer.newLine();

            // Stream over lines and write each one
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }

        System.out.println("Report written to: " + outputPath + " (" + lines.size() + " rows)");
    }
}
