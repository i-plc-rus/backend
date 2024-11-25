package org.example.catalogue.management.beautiful.code.afal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    private final Map<String, Transaction> transactionMap = new ConcurrentHashMap<>();

    public void updateTransaction(Transaction transaction) {
        transactionMap.put(transaction.id(), transaction);
    }

    public List<Transaction> getTransactions() {
        return transactionMap.values().stream().toList();
    }

    public void deleteAll() {
        transactionMap.clear();
    }
}
