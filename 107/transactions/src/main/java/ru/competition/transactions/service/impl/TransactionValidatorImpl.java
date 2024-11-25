package ru.competition.transactions.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.service.TransactionValidator;

@Component
public class TransactionValidatorImpl implements TransactionValidator {
    private static final Logger LOGGER = LogManager.getLogger(TransactionValidatorImpl.class.getName());

    @Override
    public boolean isValid(Transaction transaction) {
        if (transaction.getId() == null) {
            return false;
        }

        if (transaction.getDate() == null) {
            LOGGER.warn("Transaction with id: {} has no date", transaction.getId());
            return false;
        }

        return true;
    }
}
