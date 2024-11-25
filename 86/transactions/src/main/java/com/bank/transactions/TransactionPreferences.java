package com.bank.transactions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionPreferences {

    private final Integer largeTransactionAmount;

    public TransactionPreferences(
        @Value("${app.preferences.large-transaction-amount}") Integer largeTransactionAmount
    ) {
        this.largeTransactionAmount = largeTransactionAmount;
    }

    public Integer getLargeTransactionAmount() {
        return largeTransactionAmount;
    }
}
