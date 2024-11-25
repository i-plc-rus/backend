package com.bank.transactions.processor;

import com.bank.transactions.domain.Transaction;
import com.bank.transactions.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initial implementation of transaction processing. Still here just for comparison test.
 */
@Component
@Deprecated
public class TransactionProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionProcessor.class);
    private final TransactionRepository repository;

    @Autowired
    public TransactionProcessor(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Default implementation of transaction processing.
     * @deprecated Use {@link TransactionsProcessor#processTransactions} instead
     * @param transactions source transaction list
     */
    @Deprecated
    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.isLarge(10000)) {
                LOG.info("Processing large transaction: " + transaction.getId());
            }
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        try {
            if (transaction.isPending()) {
                transaction.process();
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            LOG.error("Error processing transaction: " + e.getMessage());
        }
    }
}
