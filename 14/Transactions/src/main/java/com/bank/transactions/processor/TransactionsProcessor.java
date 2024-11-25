package com.bank.transactions.processor;

import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.Transactions;
import com.bank.transactions.domain.TransactionProperties;
import com.bank.transactions.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionsProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionsProcessor.class);
    private final TransactionRepository repository;
    private final TransactionProperties properties;

    @Autowired
    public TransactionsProcessor(
            TransactionRepository repository,
            TransactionProperties properties
    ) {
        this.repository = repository;
        this.properties = properties;
    }

    /**
     * Optimized implementation of transaction processing.
     * @param transactions source transaction list
     */
    public void processTransactions(List<Transaction> transactions) {
        Transactions batch = new Transactions(transactions);
        List<String> ids = batch.largeTransactionIds(properties.getLargeAmountLimit());
        if (!ids.isEmpty()) {
            LOG.info("Processing {} large transaction(s): {}", ids.size(), ids);
        }
        repository.updateTransactions(batch.processed());
    }
}
