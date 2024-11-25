package com.bank.transactions;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {

    private final Map<String, Transaction> transactionMap = new ConcurrentHashMap<>();

    public void update(Transaction transaction) {
        transactionMap.put(transaction.getId(), transaction);
    }

    @SuppressWarnings("unused")
    public List<Transaction> getTransactions() {
        return transactionMap.values().stream().toList();
    }
}
