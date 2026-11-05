package com.assignment.problem1;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Entry point for Problem 1: Transaction Log Analyzer
 */
public class TransactionAnalyzerApp {

    public static void main(String[] args) {
        InputStream input = TransactionAnalyzerApp.class.getClassLoader().getResourceAsStream("transactions.csv");

        if (input == null) {
            throw new IllegalArgumentException("transactions.csv not found in classpath.");
        }
        int topN = 3; // configurable

        // --- Load ---
        TransactionLoader loader = new TransactionLoader();
        List<Transaction> transactions;
        try {
            transactions = loader.load(input);
            System.out.println("Loaded " + transactions.size() + " transactions.\n");
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            return;
        }

        TransactionService service = new TransactionService(transactions);

        // --- Report 1: Summary by category ---
        System.out.println("--- Summary by category ---");
        Map<String, Double> summary = service.summaryByCategory();
        summary.forEach((category, total) ->
                System.out.printf("  %-20s %.2f%n", category + ":", total));

        // --- Report 2: Top N by amount ---
        System.out.println("\n--- Top " + topN + " by amount ---");
        service.topNByAmount(topN).forEach(tx ->
                System.out.printf("  %-6s  %8.2f  %s%n",
                        tx.getTransactionId(), tx.getAmount(), tx.getCategory()));

        // --- Report 3: Date range ---
        LocalDate start = LocalDate.of(2024, 1, 15);
        LocalDate end   = LocalDate.of(2024, 1, 15);
        System.out.println("\n--- Transactions from " + start + " to " + end + " ---");
        service.transactionsInDateRange(start, end).forEach(tx ->
                System.out.printf("  %-6s  %-20s  %.2f%n",
                        tx.getTransactionId(), tx.getDescription(), tx.getAmount()));

        // --- Find by ID ---
        System.out.println("\n--- Find by ID: T002 ---");
        Optional<Transaction> found = service.findById("T002");
        found.ifPresentOrElse(
                tx -> System.out.println("  Found: " + tx),
                ()  -> System.out.println("  Transaction not found.")
        );
    }
}
