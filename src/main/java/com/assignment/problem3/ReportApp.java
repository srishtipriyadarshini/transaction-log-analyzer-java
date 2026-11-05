package com.assignment.problem3;

import java.io.InputStream;

import javax.sql.DataSource;

import com.assignment.problem2.DatabaseConnection;
import com.assignment.problem2.dao.DepartmentDao;
import com.assignment.problem2.dao.EmployeeDao;
import com.assignment.problem2.dao.impl.DepartmentDaoImpl;
import com.assignment.problem2.dao.impl.EmployeeDaoImpl;

/**
 * Entry point for Problem 3: Report Generator
 *
 * Flow: config file → DAO query → stream transform → CSV output file
 *
 * Run: java -cp . com.assignment.problem3.ReportApp report_config.properties
 */
public class ReportApp {

    public static void main(String[] args) {

        try {
            // Set up database and DAOs (reusing Problem 2's layer)
            DataSource ds = DatabaseConnection.createH2DataSource();
            DepartmentDao departmentDao = new DepartmentDaoImpl(ds);
            EmployeeDao   employeeDao   = new EmployeeDaoImpl(ds);

            // Generate report
            ReportGenerator generator = new ReportGenerator(employeeDao, departmentDao);
            InputStream input = ReportApp.class.getClassLoader().getResourceAsStream("report_config.properties");
            if (input == null) {
                throw new RuntimeException("report_config.properties not found");
            }
            generator.generate(input);

        } catch (Exception e) {
            System.err.println("Report generation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
