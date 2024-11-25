package com.bank.transactions.v2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;

/**
 * Логгирует сообщения
 */
public class Logger {

    private final Map<Class<?>, org.slf4j.Logger> loggers = new ConcurrentHashMap<>();

    private org.slf4j.Logger logger(Object context) {
        return loggers.computeIfAbsent(context.getClass(), LoggerFactory::getLogger);
    }

    /**
     * Логгирует сообщение с уровнем DEBUG
     *
     * @param context контекст
     * @param message сообщение
     */
    public void log(Object context, String message) {
        logger(context).debug(message);
    }

    /**
     * Логгирует сообщение с уровнем ERROR
     *
     * @param context контекст
     * @param message сообщение
     * @param throwable исключение
     */
    public void log(Object context, String message, Throwable throwable) {
        logger(context).error(message, throwable);
    }
}
