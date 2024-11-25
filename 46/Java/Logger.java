package com.example.transaction.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logError(String message, Exception e) {
        logger.error(message, e);
    }
}
