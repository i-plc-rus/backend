package com.example.proj.logger;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AppLogger {

    private final List<String> logBuffer = new CopyOnWriteArrayList<>();
    private final int bufferSize = 10000000;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void log(String message) {
        logBuffer.add(message);
        if (logBuffer.size() >= bufferSize) {
            flushLogs();
        }
    }

    public CompletableFuture<Void> flushLogs() {
        // Возвращаем CompletableFuture, чтобы можно было ждать завершения записи
        return CompletableFuture.runAsync(() -> {
            if (!logBuffer.isEmpty()) {
                logBuffer.forEach(System.out::println);  // Выводим или сохраняем логи
                logBuffer.clear();  // Очищаем буфер после записи
            }
        }, executor);
    }

    public void shutdown() {
        executor.shutdown();  // Останавливаем Executor
    }
}
