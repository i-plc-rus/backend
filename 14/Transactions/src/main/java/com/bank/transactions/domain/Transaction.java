package com.bank.transactions.domain;

import com.bank.transactions.exception.TransactionParsingException;
import com.bank.transactions.exception.WrongTransactionStatus;

import java.time.Instant;

public class Transaction {
    private final String id;
    private final double amount;
    private final Instant date;
    private TransactionStatus status;

    public Transaction(String id, double amount, Instant date, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public boolean isLarge(int limit) {
        return amount > limit;
    }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }

    public boolean isProcessed() {
        return this.status == TransactionStatus.PROCESSED;
    }

    public Transaction process() {
        if (this.status != TransactionStatus.PENDING) {
            throw new WrongTransactionStatus(TransactionStatus.PENDING, status);
        }
        this.status = TransactionStatus.PROCESSED;
        return this;
    }

    public String getId() {
        return id;
    }

    public static Transaction fromString(String str) throws TransactionParsingException {
        String[] s = str.split(" ");
        try {
            return new Transaction(
                    s[0],
                    Double.parseDouble(s[1]),
                    Instant.ofEpochMilli(Long.parseLong(s[2])),
                    TransactionStatus.getById(Integer.valueOf(s[3]).byteValue())
            );
        } catch (NumberFormatException e) {
            throw new TransactionParsingException(str, e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %f %d %d", id, amount, date.toEpochMilli(), status.getId());
    }
}
