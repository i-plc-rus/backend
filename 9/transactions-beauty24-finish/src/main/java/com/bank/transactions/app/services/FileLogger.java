package com.bank.transactions.app.services;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация логгера который сохраняет сообщения в файл
 * Этот класс реализует интерфейс Logger и сохраняет сообщения с timestamp
 */
@Component
public class FileLogger implements Logger {

    private static final String LOG_FILE = "transactions.log";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Сохраняет сообщения с текущим timestamp
     *
     * @param message сообщение для сохранения
     */
    @Override
    public void log(String message) {
        String formattedMessage = String.format("%s - LOG: %s", getCurrentTimestamp(), message);
        System.out.println(formattedMessage);
        writeLog(formattedMessage);
    }

    /**
     * Получение текущего timestamp в формате строки
     *
     * @return текущий timestamp в формате "yyyy-MM-dd HH:mm:ss"
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    /**
     * Запись лога в файл
     *
     * @param message сообщение для записи в файл
     */
    private void writeLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}
