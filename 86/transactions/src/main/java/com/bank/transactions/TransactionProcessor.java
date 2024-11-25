package com.bank.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionProcessor {

    private final TransactionRepository repository;
    private final TransactionLogger logger;
    private final TransactionPreferences preferences;

    @Autowired
    public TransactionProcessor(
        TransactionRepository repository,
        TransactionLogger logger,
        TransactionPreferences preferences
    ) {
        this.repository = repository;
        this.logger = logger;
        this.preferences = preferences;
    }

    public void processTransactions(List<Transaction> transactions) {

        if (transactions == null) return;

        transactions
            .stream()
            .parallel()
            .filter(Transaction::isPending)
            .forEach(this::processPendingTransaction);
    }

    private void processPendingTransaction(Transaction transaction) {

        try(transaction) {

            if (isLargeTransaction(transaction))
                logger.info("Processing large transaction.", transaction);

            transaction.process();
            repository.update(transaction);
        } catch (Exception e) {
            logger.error("Error processing transaction: " + e.getMessage(), transaction, e);
        }
    }

    private boolean isLargeTransaction(Transaction transaction) {
        return transaction.getAmount() >= preferences.getLargeTransactionAmount();
    }
}
