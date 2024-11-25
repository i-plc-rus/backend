package com.bank.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        transactions.forEach(this::processTransaction);
    }

    private void processTransaction(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(10000)) > 0) {
            logger.log("Processing large transaction: " + transaction.getId());
        }
        try {
            if ("PENDING".equals(transaction.getStatus())) {
                transaction.setStatus("PROCESSED");
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
        }
    }
}
