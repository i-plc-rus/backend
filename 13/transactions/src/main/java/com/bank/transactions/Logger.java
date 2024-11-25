package com.bank.transactions;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

@Component
public class Logger {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Logger.class);

    public void log(String message) {
        LOGGER.debug("Debug log: {}", message);
    }

    public void log(String message, Exception ex) {
        LOGGER.error("Error log: {}", message, ex);
    }
}
