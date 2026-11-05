package com.assignment.problem2;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

/**
 * Utility class that creates a DataSource.
 */
public class DatabaseConnection {

    public static DataSource createH2DataSource() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:empdb;DB_CLOSE_DELAY=-1");  // in-memory, stays alive
        ds.setUser("sa");
        ds.setPassword("");

        // Load schema.sql from resources
        InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("schema.sql");

        if (input == null) {
            throw new IllegalArgumentException("schema.sql not found in classpath.");
        }

        // Run schema.sql to create tables
        runSchema(ds, input);
        return ds;
    }

    private static void runSchema(DataSource ds, InputStream input) throws Exception {
        String sql = new String(input.readAllBytes());


        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            // Split on semicolons and execute each statement
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }
}
