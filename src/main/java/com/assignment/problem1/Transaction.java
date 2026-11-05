package com.assignment.problem1;

import java.time.LocalDate;

/**
 * Domain model representing a single financial transaction.
 */
public class Transaction {

    private final String transactionId;
    private final LocalDate date;
    private final String userId;
    private final String category;
    private final double amount;
    private final String description;

    public Transaction(String transactionId, LocalDate date, String userId,
                       String category, double amount, String description) {
        this.transactionId = transactionId;
        this.date = date;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public LocalDate getDate()       { return date; }
    public String getUserId()        { return userId; }
    public String getCategory()      { return category; }
    public double getAmount()        { return amount; }
    public String getDescription()   { return description; }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', date=%s, userId='%s', category='%s', amount=%.2f, desc='%s'}",
                transactionId, date, userId, category, amount, description);
    }
}
