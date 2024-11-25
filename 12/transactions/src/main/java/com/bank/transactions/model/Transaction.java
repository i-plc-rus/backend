package com.bank.transactions.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private String id;
    private double amount;
    private LocalDateTime date;
    private TransactionStatus status;

    public Transaction(String id, double amount, LocalDateTime date, TransactionStatus status) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.amount = amount;
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Transaction withId(String id) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        return this;
    }

    public Transaction withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public Transaction withDate(LocalDateTime date) {
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        return this;
    }

    public Transaction withStatus(TransactionStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        return this;
    }
}