package com.bank.transactions.v1;

public class Transaction implements com.bank.transactions.Transaction {

    private String id;
    private double amount;
    private String date;
    private String status;

    public Transaction(String id, double amount, String date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
