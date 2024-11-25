package org.example.catalogue.management.beautiful.code.afal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorsConfiguration {

    @Value("${app.small-transaction-thread-pool-size:1}")
    private int smallTransactionThreadPoolSize;

    @Value("${app.large-transaction-thread-pool-size:4}")
    private int largeTransactionThreadPoolSize;

    @Bean
    public ExecutorService smallTransactionExecutorService() {
        return Executors.newFixedThreadPool(smallTransactionThreadPoolSize);
    }

    @Bean
    public ExecutorService largeTransactionExecutorService() {
        return Executors.newFixedThreadPool(largeTransactionThreadPoolSize);
    }

    @Bean
    public BlockingQueue<Transaction> smallTransactionQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Transaction> largeTransactionQueue() {
        return new LinkedBlockingQueue<>();
    }

}
