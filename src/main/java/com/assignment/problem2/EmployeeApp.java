package com.assignment.problem2;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import com.assignment.problem2.dao.DepartmentDao;
import com.assignment.problem2.dao.EmployeeDao;
import com.assignment.problem2.dao.impl.DepartmentDaoImpl;
import com.assignment.problem2.dao.impl.EmployeeDaoImpl;
import com.assignment.problem2.domain.Department;
import com.assignment.problem2.domain.Employee;

/**
 * Entry point for Problem 2 — Employee/Department CRUD demo using JDBC + H2.
 */
public class EmployeeApp {

    public static void main(String[] args) throws Exception {
        DataSource ds = DatabaseConnection.createH2DataSource();

        DepartmentDao deptDao = new DepartmentDaoImpl(ds);
        EmployeeDao   empDao  = new EmployeeDaoImpl(ds);

        // ── Departments ────────────────────────────────────────────────────
        System.out.println("=== Creating Departments ===");
        long engId  = deptDao.create("Engineering",  "New York");
        long hrId   = deptDao.create("Human Resources", "Chicago");
        long salesId = deptDao.create("Sales", "Los Angeles");

        List<Department> departments = deptDao.findAll();
        departments.forEach(System.out::println);

        // ── Create Employees ───────────────────────────────────────────────
        System.out.println("\n=== Creating Employees ===");
        long aliceId = empDao.create("Alice Johnson", "alice.j@example.com",
                engId, 95_000.00, LocalDate.of(2021, 3, 15));
        long bobId   = empDao.create("Bob Smith",     "bob.s@example.com",
                engId, 88_000.00, LocalDate.of(2020, 7, 1));
        long carolId = empDao.create("Carol White",   "carol@example.com",
                hrId,  72_000.00, LocalDate.of(2019, 11, 20));
       
        empDao.findAll().forEach(System.out::println);

        // ── Find by ID ─────────────────────────────────────────────────────
        System.out.println("\n=== Find by ID ===");
        empDao.findById(aliceId).ifPresent(System.out::println);

        // ── Update ─────────────────────────────────────────────────────────
        System.out.println("\n=== Update Bob's salary ===");
        empDao.update(bobId, "Bob Smith", "bob.s@example.com", engId, 92_000.00);
        empDao.findById(bobId).ifPresent(System.out::println);

        // ── Find by Department ─────────────────────────────────────────────
        System.out.println("\n=== Employees in Engineering ===");
        List<Employee> engEmployees = empDao.findByDepartment(engId);
        engEmployees.forEach(System.out::println);

        // ── Delete ─────────────────────────────────────────────────────────
        System.out.println("\n=== Delete Bob ===");
        empDao.delete(bobId);
        System.out.println("Remaining employees:");
        empDao.findAll().forEach(System.out::println);

        System.out.println("\nDone.");
    }
}
