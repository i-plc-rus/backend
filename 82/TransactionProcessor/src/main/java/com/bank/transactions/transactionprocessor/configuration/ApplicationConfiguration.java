package com.bank.transactions.transactionprocessor.configuration;

import com.bank.transactions.transactionprocessor.service.processor.amount.AmountTypeResolverProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@EnableConfigurationProperties({AmountTypeResolverProperty.class, TransactionExecutorProperty.class})
public class ApplicationConfiguration {

    @Bean
    public ExecutorService transactionExecutor(TransactionExecutorProperty property) {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(property.queueSize());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                property.threadPoolSize(),
                property.threadPoolSize(),
                property.keepAliveTime(), TimeUnit.SECONDS,
                blockingQueue,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
