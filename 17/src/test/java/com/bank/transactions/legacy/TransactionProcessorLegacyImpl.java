
package com.bank.transactions.legacy;

import com.bank.transactions.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TransactionProcessorLegacyImpl {

    private final TransactionRepositoryLegacyImpl repository;
    private final LoggerLegacyImpl logger;

    @Autowired
    public TransactionProcessorLegacyImpl(TransactionRepositoryLegacyImpl repository, LoggerLegacyImpl logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 10000) {
                logger.log("Processing large transaction: " + transaction.getId());
            }
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        try {
            if (transaction.getStatus().equals("PENDING")) {
                transaction.setStatus("PROCESSED");
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
        }
    }
}
