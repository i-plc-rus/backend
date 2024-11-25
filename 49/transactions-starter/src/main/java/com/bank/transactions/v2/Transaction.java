package com.bank.transactions.v2;

public class Transaction implements com.bank.transactions.Transaction {

    private final String id;
    private final Double amount;
    private final String date;
    private final String status;

    public Transaction(String id, Double amount, String date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public Transaction(com.bank.transactions.Transaction transaction, String status) {
        this(transaction.getId(), transaction.getAmount(), transaction.getDate(), status);
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
}
