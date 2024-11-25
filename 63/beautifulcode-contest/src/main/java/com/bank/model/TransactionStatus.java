package com.bank.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionStatus {
    PENDING,
    COMPLETED;

    @JsonCreator
    public static TransactionStatus toStatus(String val) {
        return TransactionStatus.valueOf(val.trim().toUpperCase());
    }
}
