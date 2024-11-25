package com.bank.transactions.exception;

public class TransactionRepositoryException extends RuntimeException {
    public TransactionRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
