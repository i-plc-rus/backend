package com.example.sbertech.dao;

import com.example.sbertech.pojo.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Main repository. Thread-safe.
 */
@Component
@Qualifier("TransactionRepositoryMine")
public class TransactionRepositoryMine implements TransactionRepository {

    protected static final int INIT_CAPACITY = 10_000_000;

    protected final List<Transaction> transactions = new ArrayList<>(INIT_CAPACITY);

    @Override
    public void updateTransaction(Transaction transaction) {
        synchronized (transactions) {
            transactions.add(transaction);
        }
    }

    @Override
    public int countTransactions() {
        return transactions.size();
    }
}
