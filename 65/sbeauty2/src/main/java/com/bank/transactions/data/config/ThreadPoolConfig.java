package com.bank.transactions.data.config;

import com.bank.transactions.data.config.props.ThreadPoolConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

@Configuration
public class ThreadPoolConfig {

    @Bean
    @ConditionalOnProperty(prefix = "transaction.processor", name = "type", havingValue = "PARALLEL")
    public ForkJoinPool transactionProcessingThreadPool(ThreadPoolConfigProperties props) {
        return new ForkJoinPool(Math.max(Runtime.getRuntime().availableProcessors(), props.getMaxParallelism()));
    }
}
