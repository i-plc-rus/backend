package com.bank.transactions.repository;

import com.bank.transactions.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Initial implementation of Transaction repository.
 */
@Deprecated
@Repository
public class TransactionInMemoryRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public void updateTransactions(List<Transaction> transactions) {
        this.transactions.addAll(transactions);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
