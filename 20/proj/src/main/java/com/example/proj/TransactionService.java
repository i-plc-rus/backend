package com.example.proj;

import com.example.proj.exceptions.TransactionAlreadyProcessedException;
import com.example.proj.exceptions.TransactionNotFoundException;
import com.example.proj.exceptions.TransactionProcessingException;
import com.example.proj.logger.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionService {

    private final TransactionRepository repository;
    private final AppLogger logger;

    @Autowired
    public TransactionService(TransactionRepository repository, AppLogger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        if (transaction.getStatus() == TransactionStatus.PROCESSED) {
            throw new TransactionAlreadyProcessedException("Transaction with ID " + transaction.getId() + " is already processed.");
        }

        if (transaction.getAmount() < 0) {
            throw new TransactionProcessingException("Transaction with ID " + transaction.getId() + " has invalid amount: " + transaction.getAmount());
        }

        try {
            logLargeTransaction(transaction);
            processTransactionStatus(transaction);
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
            throw new TransactionProcessingException("Failed to process transaction: " + transaction.getId());
        }
    }


    private void logLargeTransaction(Transaction transaction) {
        if (transaction.getAmount() > 10000) {
            logger.log("Processing large transaction: " + transaction.getId());
        }
    }

    private void processTransactionStatus(Transaction transaction) {
        if (transaction.getStatus() == TransactionStatus.PENDING) {
            transaction.setStatus(TransactionStatus.PROCESSED);
            repository.save(transaction);
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transaction.getId() + " not found.");
        }
    }
}
