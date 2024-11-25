package com.example.sbertech.service;

import com.example.sbertech.pojo.Transaction;

import java.util.List;

/**
 * Interface for transaction operation services.
 */
public interface TransactionService {
    int LARGE_TRANSACTION_STARTS_FROM_AMOUNT_IN_PENNY = 10_000 * 100;

    /**
     * Process list of transactions.
     *
     * @param transactions
     */
    void processTransactions(List<Transaction> transactions);

    /**
     * Process one transaction.
     *
     * @param transaction
     */
    void processTransaction(Transaction transaction);

    /**
     * Size of transaction structure.
     *
     * @return size
     */
    int countTransactions();
}
