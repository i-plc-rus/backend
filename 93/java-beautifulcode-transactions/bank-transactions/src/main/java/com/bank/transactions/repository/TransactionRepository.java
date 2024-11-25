package com.bank.transactions.repository;

import com.bank.transactions.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> getTransactionById(Long id);
    void saveTransaction(Transaction transaction);
    Optional<List<Transaction>> getAllTransactions();
    int size();
}
