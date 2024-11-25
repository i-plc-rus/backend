package com.bank.common.exceptions;

/**
 * Непроверяемое исключение для приложений банка
 */
public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
