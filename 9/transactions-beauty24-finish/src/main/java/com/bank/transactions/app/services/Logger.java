package com.bank.transactions.app.services;

/**
 * Интерфейс для логгирования сообщений
 * Этот интерфейс предоставляет метод для логгирования сообщений в определенном формате
 */
public interface Logger {

    /**
     * Логгирование сообщения
     *
     * @param message сообщение для логгирования
     */
    void log(String message);
}

