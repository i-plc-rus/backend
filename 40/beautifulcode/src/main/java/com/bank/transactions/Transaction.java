package com.bank.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {

    @NonNull
    private final Long id;
    @NonNull
    private final BigDecimal amount;
    @NonNull
    private final LocalDate date;
    @NonNull
    private TransactionStatus status;

    @Override
    public String toString() {
        return "Transaction ID: " + this.id + " Status: " + this.status;
    }

    public String getInfoLarge() {
        return "Processing large transaction ID: " + this.id;
    }
}