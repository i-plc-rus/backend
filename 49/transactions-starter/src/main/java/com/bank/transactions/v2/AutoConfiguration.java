package com.bank.transactions.v2;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration("autoConfigurationV2")
@EnableAspectJAutoProxy
@EnableConfigurationProperties({
    TransactionsProperties.class
})
public class AutoConfiguration {

    @Bean
    public Banner banner(TransactionsProperties transactionsProperties) {
        return new Banner(transactionsProperties);
    }

    @Bean("loggerV2")
    public Logger logger() {
        return new Logger();
    }

    @Bean("transactionRepositoryV2")
    public TransactionRepository transactionRepository() {
        return new TransactionRepository();
    }

    @Bean("transactionProcessorV2")
    public com.bank.transactions.TransactionProcessor transactionProcessor(
        TransactionsProperties transactionsProperties,
        TransactionRepository transactionRepository,
        Logger logger) {
        return new TransactionProcessor(transactionsProperties, transactionRepository, logger);
    }

    @Bean
    public ExceptionHandler exceptionHandler(Logger logger) {
        return new ExceptionHandler(logger);
    }
}
