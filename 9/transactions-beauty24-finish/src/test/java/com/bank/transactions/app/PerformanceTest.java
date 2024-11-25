package com.bank.transactions.app;

import com.bank.transactions.app.models.Transaction;
import com.bank.transactions.app.repositories.TransactionRepository;
import com.bank.transactions.app.services.FileLogger;
import com.bank.transactions.app.services.TransactionProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PerformanceTest {

    @Test
    void testProcessLargeNumberOfTransactions() {
        TransactionProcessor processor = new TransactionProcessor(new TransactionRepository(), new FileLogger());
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            transactions.add(new Transaction(String.valueOf(i), 100.0, "2023-10-01", "PENDING"));
        }

        long startTime = System.currentTimeMillis();
        processor.processTransactions(transactions);
        long endTime = System.currentTimeMillis();

        System.out.println("Processing time for 10,000 transactions: " + (endTime - startTime) + " ms");
    }
}
