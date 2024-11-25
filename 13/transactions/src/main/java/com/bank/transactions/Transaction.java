package com.bank.transactions;

import java.util.Objects;

public class Transaction {
    private final String id;
    private final double amount;
    private final String date;
    private String status;

    public Transaction(String id, double amount, String date, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + Double.valueOf(amount).hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                amount == that.amount &&
                Objects.equals(date, that.date) &&
                Objects.equals(status, that.status);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status + '\'' +
                '}';
    }
}
