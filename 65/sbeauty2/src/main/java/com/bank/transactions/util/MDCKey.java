package com.bank.transactions.util;

public enum MDCKey {
    TRANSACTION_ID("transactionId");

    MDCKey(String name) {
        this.keyName = name;
    }

    private final String keyName;

    public String getKeyName() {
        return keyName;
    }
}
