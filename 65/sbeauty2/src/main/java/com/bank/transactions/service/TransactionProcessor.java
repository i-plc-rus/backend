package com.bank.transactions.service;

import com.bank.transactions.data.model.Transaction;

public interface TransactionProcessor {

    /**
     * Transaction processor
     *
     * @param transaction - transaction array
     */
    void processTransaction(Transaction transaction);
}
