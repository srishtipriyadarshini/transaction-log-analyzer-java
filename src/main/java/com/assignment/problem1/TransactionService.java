package com.assignment.problem1;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer providing all reporting and query operations on transactions.
 */
public class TransactionService {

    private final List<Transaction> transactions;

    public TransactionService(List<Transaction> transactions) {
        this.transactions = Collections.unmodifiableList(transactions);
    }

    // -----------------------------------------------------------------------
    // Report 1: Total amount per category
    // Uses: Collectors.groupingBy + Collectors.summingDouble
    // -----------------------------------------------------------------------

    /**
     * Returns a map of category → total amount.
     * Example: { "GROCERIES" -> 250.00, "TRANSPORT" -> 45.00 }
     */
    public Map<String, Double> summaryByCategory() {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,           // classifier: group by category
                        Collectors.summingDouble(Transaction::getAmount) // downstream: sum amounts
                ));
    }

    // -----------------------------------------------------------------------
    // Report 2: Top N transactions by amount (descending)
    // Uses: sorted (with Comparator), limit, collect to List
    // -----------------------------------------------------------------------

    /**
     * Returns the top N transactions sorted by amount descending.
     * @param n number of results to return
     */
    public List<Transaction> topNByAmount(int n) {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed()) // descending
                .limit(n)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------
    // Report 3: Transactions in a date range (inclusive)
    // Uses: filter with lambda, collect to List
    // -----------------------------------------------------------------------

    /**
     * Returns all transactions whose date falls within [startDate, endDate] inclusive.
     */
    public List<Transaction> transactionsInDateRange(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(tx -> !tx.getDate().isBefore(startDate)  // date >= startDate
                           && !tx.getDate().isAfter(endDate))    // date <= endDate
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------
    // Find by ID — uses Optional
    // -----------------------------------------------------------------------

    /**
     * Finds a transaction by its ID.
     * Returns Optional.empty() if not found, so caller handles the "missing" case safely.
     */
    public Optional<Transaction> findById(String transactionId) {
        return transactions.stream()
                .filter(tx -> tx.getTransactionId().equalsIgnoreCase(transactionId))
                .findFirst(); // returns Optional<Transaction>
    }
}
