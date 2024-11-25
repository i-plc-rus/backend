package com.bank.transactions;

import java.time.LocalDate;

public class Transaction implements AutoCloseable {

    private final String id;
    private final double amount;
    private final String date;
    private TransactionStatus status;

    public Transaction(String id, double amount, String date, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public Transaction(String id, double amount) {
        this.id = id;
        this.amount = amount;
        this.date = LocalDate.now().toString();
        this.status = TransactionStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    @SuppressWarnings("unused")
    public String getDate() {
        return date;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }

    public void process() {
        this.status = TransactionStatus.PROCESSED;
    }

    @Override
    public void close() {
        this.status = TransactionStatus.COMPLETED;
    }
}
