package com.bank.transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Logger {

    private static final Logger logger = LoggerFactory.getLogger(Logger.class);

    public void log(String message) {
        logger.info("LOG; Aslan555: ", message);
    }
}
