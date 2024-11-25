package com.bank.transactions.service.impl;

import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionProcessorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.bank.transactions.data.model.TransactionStatus.PENDING;

@Component
public class TransactionProcessorValidatorImpl implements TransactionProcessorValidator {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessorValidatorImpl.class);

    @Override
    public boolean validateForProcessing(Transaction transaction) {
        if (transaction == null) {
            logger.error("Transaction is null"); // TODO may be throw new UnsupportedOperationException
            return false;
        }
        if (!transaction.isPending()) {
            logger.warn("Transaction is in {} and is not in {} status", transaction.getStatus(), PENDING);
            return false;
        }
        if (transaction.hasLargeAmount()) {
            logger.info("Processing large transaction: {}", transaction.getId());
        }
        return true;
    }
}
