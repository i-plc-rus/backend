package com.bank.exception;

public class BatchSaveException extends RuntimeException {
    public BatchSaveException(String message, Exception cause) {
        super(message, cause);
    }
}
