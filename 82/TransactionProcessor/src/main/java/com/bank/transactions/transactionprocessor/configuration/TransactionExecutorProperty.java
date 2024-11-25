package com.bank.transactions.transactionprocessor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "transaction.executor")
public record TransactionExecutorProperty(
        int queueSize,
        int threadPoolSize,
        long keepAliveTime
) {
}
