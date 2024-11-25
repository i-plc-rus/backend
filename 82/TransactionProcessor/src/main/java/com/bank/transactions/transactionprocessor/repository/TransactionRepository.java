package com.bank.transactions.transactionprocessor.repository;

import com.bank.transactions.transactionprocessor.repository.entity.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    void save(Transaction transaction);

    Optional<Transaction> findById(UUID id);

    void deleteById(UUID id);

    int count();

    void clear();
}
