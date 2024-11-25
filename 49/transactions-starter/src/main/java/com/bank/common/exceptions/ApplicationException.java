package com.bank.common.exceptions;

/**
 * Проверяемое исключение для приложений банка
 */
public class ApplicationException extends Exception {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
