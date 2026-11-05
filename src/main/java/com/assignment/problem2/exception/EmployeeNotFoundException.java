package com.assignment.problem2.exception;

/**
 * Thrown when an employee is looked up but does not exist in the database.
 * Extends RuntimeException so callers are not forced to catch it everywhere,
 * but it clearly signals a "not found" condition.
 */
public class EmployeeNotFoundException extends RuntimeException {

    private final long employeeId;

    public EmployeeNotFoundException(long employeeId) {
        super("Employee not found with id: " + employeeId);
        this.employeeId = employeeId;
    }

    public long getEmployeeId() {
        return employeeId;
    }
}
