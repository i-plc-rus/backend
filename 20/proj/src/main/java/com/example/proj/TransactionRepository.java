package com.example.proj;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    public void save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    public Transaction findById(String id) {
        return transactions.get(id);
    }

    public List<Transaction> getAll() {
        return new ArrayList<>(transactions.values());
    }
}
