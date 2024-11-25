package com.bank.transactions;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository implements AutoCloseable {
    private final Logger logger;

    public TransactionRepository(Logger logger) {
        this.logger = logger;
    }

    private final List<Transaction> transactions = new ArrayList<>();

    synchronized public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    synchronized public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void close(){
        logger.log("TransactionRepository is closed");
    }
}
