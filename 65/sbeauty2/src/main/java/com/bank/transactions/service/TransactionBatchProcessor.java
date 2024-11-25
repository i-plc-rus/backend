package com.bank.transactions.service;

import com.bank.transactions.data.model.Transaction;

import java.util.Collection;

public interface TransactionBatchProcessor {

    /**
     * Transaction processor that handles batches of transactions
     *
     * @param transactions - transactions array
     */
    void processTransactions(Collection<Transaction> transactions);
}
