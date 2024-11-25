package com.bank.transactions.repo;

public interface TransactionRepository {
    TransactionSession openSession();
}
