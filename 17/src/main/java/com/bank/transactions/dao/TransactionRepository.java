package com.bank.transactions.dao;

import com.bank.transactions.dto.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionRepository {
    private final Set<Transaction> transactions = Collections.synchronizedSet(new HashSet<>());

    public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        synchronized (transactions) {
            return transactions.stream().toList();
        }
    }

    public void deleteAll() {
        synchronized (transactions) {
            transactions.clear();
        }
    }

    public void saveAll(List<Transaction> transactions) {
        transactions.forEach(this::updateTransaction);
    }
}
