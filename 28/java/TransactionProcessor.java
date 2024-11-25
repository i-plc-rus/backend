package com.bank.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TransactionProcessor {

    private final TransactionRepository repository;
    private final Logger logger;
    private static final double LARGE_TRANSACTION_THRESHOLD = 10000;

    @Autowired
    public TransactionProcessor(TransactionRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public void processTransactions(List<Transaction> transactions) {
        transactions.stream()
                .filter(transaction -> "PENDING".equals(transaction.getStatus()))
                .forEach(this::processTransaction);
    }

    private void processTransaction(Transaction transaction) {
        if (isLargeTransaction(transaction)) {
            logger.log("Processing large transaction: " + transaction.getId());
        }

        try {
            transaction.setStatus("PROCESSED");
            repository.updateTransaction(transaction);
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
        }
    }

    private boolean isLargeTransaction(Transaction transaction) {
        return transaction.getAmount() > LARGE_TRANSACTION_THRESHOLD;
    }
}