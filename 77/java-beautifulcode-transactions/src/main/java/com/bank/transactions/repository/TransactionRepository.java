package com.bank.transactions.repository;

import com.bank.transactions.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findById(Long id);

    void save(Transaction transaction);

    int size();
}
