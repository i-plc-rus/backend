package com.bank.transactions;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bank.transactions.TransactionStatus.PENDING;
import static com.bank.transactions.TransactionStatus.PROCESSED;

import java.util.List;

@Component
public class TransactionProcessor {
    private final TransactionRepository repository;
    private final Logger logger;

    @Autowired
    public TransactionProcessor(TransactionRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public void processTransactions(List<Transaction> transactions) {
        CollectionUtils.emptyIfNull(transactions).forEach(transaction -> {
            logBigTransaction(transaction);
            processTransaction(transaction);
        });
    }

    private void logBigTransaction(Transaction transaction) {
        if (PENDING.name().equals(transaction.getStatus()) && transaction.getAmount() > 10000) {
            logger.log("Processing large transaction: " + transaction.getId());
        }
    }

    private void processTransaction(Transaction transaction) {
        try (repository) {
            if (PENDING.name().equals(transaction.getStatus())) {
                transaction.setStatus(PROCESSED.name());
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            //just write error in log
            logger.log("Error processing transaction: " + e.getMessage(), e);
        }
    }
}

