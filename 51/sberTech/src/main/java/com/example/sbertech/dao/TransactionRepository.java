package com.example.sbertech.dao;

import com.example.sbertech.pojo.Transaction;

/**
 * Repository working with transactions.
 */
public interface TransactionRepository {

    /**
     * Updates existing transaction.
     *
     * @param transaction transaction
     */
    void updateTransaction(Transaction transaction);

    /**
     * Counts transactions.
     *
     * @return size
     */
    int countTransactions();
}
