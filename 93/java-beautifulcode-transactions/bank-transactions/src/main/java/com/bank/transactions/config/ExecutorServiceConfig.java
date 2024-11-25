package com.bank.transactions.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {
    private final int threadPoolSize;

    public ExecutorServiceConfig(@Value("${application.threads.pool.size}") int threadPoolSize) {
        if (threadPoolSize <= 0) {
            threadPoolSize = 4;
        }
        this.threadPoolSize = threadPoolSize;
    }

    @Bean(name = "FixedThreadPoolTaskExecutor")
    ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}

