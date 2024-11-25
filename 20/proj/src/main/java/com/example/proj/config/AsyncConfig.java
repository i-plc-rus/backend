package com.example.proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Настройки пула потоков
        executor.setCorePoolSize(10);         // Количество потоков в пуле по умолчанию
        executor.setMaxPoolSize(50);         // Максимальное количество потоков
        executor.setQueueCapacity(1000);      // Очередь задач, ожидающих выполнения
        executor.setThreadNamePrefix("AsyncThread-"); // Префикс для имени потоков

        // Инициализируем пул после задания параметров
        executor.initialize();
        return executor;
    }
}
