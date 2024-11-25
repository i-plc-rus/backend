package com.bank.transactions.repo;

import com.bank.transactions.model.Transaction;

import java.util.List;

public interface TransactionSession extends AutoCloseable {
    void updateTransactions(List<Transaction> transactions);
}