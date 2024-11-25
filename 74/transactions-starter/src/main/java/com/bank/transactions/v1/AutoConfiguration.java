package com.bank.transactions.v1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("autoConfigurationV1")
public class AutoConfiguration {

    @Bean("loggerV1")
    public Logger logger() {
        return new Logger();
    }

    @Bean("transactionRepositoryV1")
    public TransactionRepository transactionRepository() {
        return new TransactionRepository();
    }

    @Bean("transactionProcessorV1")
    public com.bank.transactions.TransactionProcessor transactionProcessor(TransactionRepository repository, Logger logger) {
        return new TransactionProcessor(repository, logger);
    }
}
