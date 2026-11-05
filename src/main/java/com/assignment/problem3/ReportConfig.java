package com.assignment.problem3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads report configuration from a .properties file.
 */
public class ReportConfig {

    private final Properties props = new Properties();

    /**
     * Loads configuration from the given InputStream.
     */
    public ReportConfig(InputStream configFilePath) throws IOException {
        // try-with-resources closes the stream automatically
         if (configFilePath == null) {
            throw new IllegalArgumentException("Config file InputStream is null");
        }

        // try-with-resources closes stream automatically
        try (InputStream in = configFilePath) {
            props.load(in);
        }
    }

    public String getReportType() {
        return props.getProperty("report.type", "EMPLOYEES_BY_DEPARTMENT");
    }

    public long getDepartmentId() {
        return Long.parseLong(props.getProperty("department.id", "1"));
    }

    public String getOutputFile() {
        return props.getProperty("output.file", "report_output.csv");
    }

    @Override
    public String toString() {
        return String.format("ReportConfig{type='%s', deptId=%d, output='%s'}",
                getReportType(), getDepartmentId(), getOutputFile());
    }
}
