package com.bank.transactions;

import com.bank.transactions.domain.TransactionProperties;
import com.bank.transactions.repository.TransactionFileRepository;
import com.bank.transactions.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(TransactionProperties.class)
public class TransactionsApplication {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
    }

    @Bean
    public TransactionRepository repository(TransactionProperties properties) {
        return new TransactionFileRepository(properties);
    }
}
