package com.example.proj;

public class Transaction {
    private final String id;
    private double amount;
    private final String date;
    private TransactionStatus  status;

    public Transaction(String id, double amount, String date, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
