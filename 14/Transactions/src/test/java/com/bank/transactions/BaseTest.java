package com.bank.transactions;

import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionProperties;
import com.bank.transactions.domain.TransactionStatus;
import com.bank.transactions.processor.TransactionsProcessor;
import com.bank.transactions.processor.TransactionProcessor;
import com.bank.transactions.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class BaseTest {
    @Autowired
    protected TransactionProcessor oldProcessor;
    @Autowired
    protected TransactionsProcessor newProcessor;
    @Autowired
    protected TransactionRepository repository;
    @Autowired
    protected TransactionProperties properties;

    protected List<Transaction> createPendingTransactions(int number) {
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            transactions.add(new Transaction(
                    String.valueOf(i),
                    random.nextDouble(999),
                    Instant.now(),
                    TransactionStatus.PENDING
            ));
        }
        return transactions;
    }

    @BeforeEach
    public void setUp() {
        clearStorage();
    }

    protected void clearStorage() {
        try {
            new FileWriter(properties.getStorageName(), false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
