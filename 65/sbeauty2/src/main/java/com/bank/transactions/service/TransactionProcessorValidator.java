package com.bank.transactions.service;

import com.bank.transactions.data.model.Transaction;

public interface TransactionProcessorValidator {

    /**
     * Single transaction validator for processing
     *
     * @param transaction - transaction for processing
     * @return - validation result
     */
    boolean validateForProcessing(Transaction transaction);
}
