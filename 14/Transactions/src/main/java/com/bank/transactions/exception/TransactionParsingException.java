package com.bank.transactions.exception;

public class TransactionParsingException extends Exception {
    public TransactionParsingException(String source, Throwable cause) {
        super(String.format("Transaction cannot be parsed from string=%s", source), cause);
    }
}
