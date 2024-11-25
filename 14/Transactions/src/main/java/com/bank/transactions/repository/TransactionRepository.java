package com.bank.transactions.repository;

import com.bank.transactions.domain.Transaction;

import java.util.List;

public interface TransactionRepository {
    void updateTransaction(Transaction transaction);
    void updateTransactions(List<Transaction> transactions);

    List<Transaction> getTransactions();
}
