package com.bank.transactions.service;

import com.bank.transactions.model.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TransactionProcessor {
    Transaction getTransactionById(Long id);
    List<Transaction> getAllTransactions(int count);
    CompletableFuture<Transaction> addTransaction(double amount);
    void processTransactions(Collection<Long> ids);

    void processTransaction(Long id);
    int getTransactionsCount();
}
