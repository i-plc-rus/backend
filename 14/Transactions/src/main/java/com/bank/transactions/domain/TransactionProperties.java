package com.bank.transactions.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "transactions")
public class TransactionProperties {
    private final int largeAmountLimit;
    private final String storageName;

    public TransactionProperties(int largeAmountLimit, String storageName) {
        this.largeAmountLimit = largeAmountLimit;
        this.storageName = storageName;
    }

    public int getLargeAmountLimit() {
        return largeAmountLimit;
    }

    public String getStorageName() {
        return storageName;
    }
}
