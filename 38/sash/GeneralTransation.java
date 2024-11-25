package com.bank.transactions;

import org.springframework.stereotype.Component;

@Component
public class Logger {
    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}

package com.bank.transactions;

public class Transaction {
    private String id;
    private double amount;
    private String date;
    private String status;

    public Transaction(String id, double amount, String date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.bank.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TransactionProcessor {

    private final TransactionRepository repository;
    private final Logger logger;

    @Autowired
    public TransactionProcessor(TransactionRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 10000) {
                logger.log("Processing large transaction: " + transaction.getId());
            }
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        try {
            if ("PENDING".equals(transaction.getStatus())) {
                transaction.setStatus("PROCESSED");
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
        }
    }
}

package com.bank.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor processor;

    @Test
    public void testProcessTransactions() {
        var transactions = List.of(
                new Transaction("1", 5000, "2023-01-01", "PENDING"),
                new Transaction("2", 15000, "2023-01-02", "PENDING"),
                new Transaction("3", 2000, "2023-01-03", "COMPLETED")
        );

        processor.processTransactions(transactions);

        transactions.forEach(t -> System.out.printf("Transaction ID: %s | Status: %s%n", t.getId(), t.getStatus()));
    }
}

package com.bank.transactions;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

    List<Transaction> findByAmountGreaterThan(double amount);

    List<Transaction> findByStatus(String status);

    void updateTransaction(Transaction transaction);
}