package com.bank.transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionLogger {

    private final Logger logger = LoggerFactory.getLogger("com.bank.transaction");

    public void info(String message, Transaction transaction) {
        logger.info(makeLogMessageWithTransactionId(transaction, message));
    }

    public void error(String message, Transaction transaction, Throwable t) {
        logger.error(makeLogMessageWithTransactionId(transaction, message), t);
    }

    private String makeLogMessageWithTransactionId(Transaction transaction, String message) {

        String id = null;
        try {
            id = transaction.getId();
        } catch (Exception ignored) {
            // do nothing
        }

        return "Transaction id: " + id + ". " + message;
    }
}
