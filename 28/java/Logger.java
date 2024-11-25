package com.bank.transactions;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Logger {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void log(String message) {
        System.out.println(formatLogMessage("INFO", message));
    }

    public void logError(String message) {
        System.err.println(formatLogMessage("ERROR", message));
    }

    private String formatLogMessage(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        return String.format("%s [%s]: %s", timestamp, level, message);
    }
}