package com.bank.transactions.transactionprocessor.exception;

import com.bank.transactions.transactionprocessor.enums.TransactionType;

public class ProcessorNotFoundException extends RuntimeException {
    public ProcessorNotFoundException(TransactionType type) {
        super("Processor not found for transaction type: " + type);
    }
}
