package com.bank.transactions.service.impl;

import com.bank.transactions.data.dao.TransactionRepository;
import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionProcessor;
import com.bank.transactions.service.TransactionProcessorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bank.transactions.data.model.TransactionStatus.*;
import static com.bank.transactions.util.MDCKey.TRANSACTION_ID;

@Service
public class TransactionProcessorImpl implements TransactionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessorImpl.class);

    private final TransactionRepository repository;
    private final TransactionProcessorValidator validator;

    public TransactionProcessorImpl(TransactionRepository repository, TransactionProcessorValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void processTransaction(Transaction transaction) {
        putMDCIfNeeded(transaction);

        if (!validator.validateForProcessing(transaction)) {
            return;
        }

        try {
            transaction.setStatus(IN_PROGRESS);
            repository.updateTransaction(transaction);
            transaction.setStatus(PROCESSED);
            logger.info("Transaction is processed");
        } catch (Exception ex) {
            transaction.setStatus(ERROR);
            logger.error("Error processing transaction: {}", ex.getMessage(), ex);
        } finally {
            MDC.remove(TRANSACTION_ID.getKeyName());
        }
    }

    private static void putMDCIfNeeded(Transaction transaction) {
        Optional.ofNullable(transaction)
                .map(Transaction::getId)
                .ifPresent(id -> MDC.put(TRANSACTION_ID.getKeyName(), id));
    }
}
