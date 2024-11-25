package com.bank.transactions.util;

import com.bank.transactions.model.Transaction;
import com.bank.transactions.model.TransactionStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public final class TransactionFactory {

    public @NonNull Transaction createTransaction(double amount, LocalDateTime date) {
        final String id = generateUniqueId();
        final TransactionStatus status = TransactionStatus.PENDING;

        return new Transaction(id, amount, date, status);
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
