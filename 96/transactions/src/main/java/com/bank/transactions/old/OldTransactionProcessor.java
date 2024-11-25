
package com.bank.transactions.old;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OldTransactionProcessor {

    private final OldTransactionRepository repository;
    private final OldLogger logger;

    @Autowired
    public OldTransactionProcessor(OldTransactionRepository repository, OldLogger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public void processTransactions(List<OldTransaction> transactions) {
        for (OldTransaction transaction : transactions) {
            if (transaction.getAmount() > 10000) {
                logger.log("Processing large transaction: " + transaction.getId());
            }
            processTransaction(transaction);
        }
    }

    private void processTransaction(OldTransaction transaction) {
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
