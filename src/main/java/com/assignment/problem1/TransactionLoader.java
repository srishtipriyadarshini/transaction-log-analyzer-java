package com.assignment.problem1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Responsible for loading transactions from a CSV file.
 */
public class TransactionLoader {

    /**
     * Loads all valid transactions from the given CSV file path.
     */
    public List<Transaction> load(InputStream filePath) throws IOException {

        List<Transaction> transactions = new ArrayList<>();

        // try-with-resources ensures BufferedReader is closed automatically
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                // Skip blank lines
                if (line.isBlank()) continue;

                // Try to parse; skip malformed lines (log them instead of crashing)
                Optional<Transaction> tx = parseLine(line);
                tx.ifPresent(transactions::add);
            }
        }

        return transactions;
    }

    /**
     * Parses one CSV line into a Transaction.
     * Returns Optional.empty() for any malformed line.
     *
     * The CSV may have quoted fields that contain commas, e.g.:
     *   T001,2024-01-15,U100,GROCERIES,125.50,"Bakery, bread"
     */
    private Optional<Transaction> parseLine(String line) {
        try {
            // Split respecting quoted commas
            String[] parts = splitCsvLine(line);
            if (parts.length < 6) {
                System.err.println("[WARN] Skipping malformed line (not enough columns): " + line);
                return Optional.empty();
            }

            String transactionId = parts[0].trim();
            LocalDate date       = LocalDate.parse(parts[1].trim());   // yyyy-MM-dd default
            String userId        = parts[2].trim();
            String category      = parts[3].trim();
            double amount        = Double.parseDouble(parts[4].trim());
            String description   = parts[5].trim().replaceAll("^\"|\"$", ""); // strip surrounding quotes

            return Optional.of(new Transaction(transactionId, date, userId, category, amount, description));

        } catch (DateTimeParseException | NumberFormatException e) {
            System.err.println("[WARN] Skipping malformed line (" + e.getMessage() + "): " + line);
            return Optional.empty();
        }
    }

    /**
     * Splits a CSV line, respecting double-quoted fields that may contain commas.
     */
    private String[] splitCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString()); // add last field
        return tokens.toArray(new String[0]);
    }
}
