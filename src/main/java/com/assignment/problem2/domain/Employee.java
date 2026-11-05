package com.assignment.problem2.domain;

import java.time.LocalDate;

/**
 * Domain object representing an Employee.
 */
public class Employee {

    private final long id;
    private final String name;
    private final String email;
    private final long departmentId;
    private final double salary;
    private final LocalDate hireDate;

    public Employee(long id, String name, String email,
                    long departmentId, double salary, LocalDate hireDate) {
        this.id           = id;
        this.name         = name;
        this.email        = email;
        this.departmentId = departmentId;
        this.salary       = salary;
        this.hireDate     = hireDate;
    }

    public long getId()            { return id; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public long getDepartmentId()  { return departmentId; }
    public double getSalary()      { return salary; }
    public LocalDate getHireDate() { return hireDate; }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', email='%s', deptId=%d, salary=%.2f, hireDate=%s}",
                id, name, email, departmentId, salary, hireDate);
    }
}
