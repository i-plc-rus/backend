
package com.bank.transactions;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();

    public void updateTransaction(Transaction transaction) throws IllegalArgumentException {
        if (transactions.containsKey(transaction.getId())) {
            throw new IllegalArgumentException("already exists!");
        }
        transactions.put(transaction.getId(), transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions.values().stream()
                .toList();
    }

    public Transaction getTransaction(long id) {
        return transactions.getOrDefault(id, null);
    }
}
