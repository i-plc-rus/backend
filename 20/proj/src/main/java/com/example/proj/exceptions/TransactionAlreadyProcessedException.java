package com.example.proj.exceptions;

public class TransactionAlreadyProcessedException extends RuntimeException {
    public TransactionAlreadyProcessedException(String message) {
        super(message);
    }
}
