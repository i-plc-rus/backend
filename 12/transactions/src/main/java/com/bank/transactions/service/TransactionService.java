package com.bank.transactions.service;

import com.bank.transactions.model.Transaction;

public interface TransactionService {
    void processTransaction(Transaction transaction);
}