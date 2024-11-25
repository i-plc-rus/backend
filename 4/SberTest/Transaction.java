package com.bank.transactions;

import java.math.BigDecimal;

public class Transaction {
    private String id;
    private BigDecimal amount;
    private String date;
    private String status;

    public Transaction(String id, BigDecimal amount, String date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
