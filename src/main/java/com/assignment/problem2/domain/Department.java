package com.assignment.problem2.domain;

/**
 * Domain object representing a Department.
 */
public class Department {

    private final long id;
    private final String name;
    private final String location;

    public Department(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public long getId()       { return id; }
    public String getName()   { return name; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return String.format("Department{id=%d, name='%s', location='%s'}", id, name, location);
    }
}
