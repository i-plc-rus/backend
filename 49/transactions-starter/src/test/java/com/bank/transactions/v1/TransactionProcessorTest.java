package com.bank.transactions.v1;

import com.bank.transactions.Transaction;
import com.bank.transactions.TransactionProcessor;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AutoConfiguration.class)
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor processor;

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction("1", 5000, "2023-01-01", "PENDING"));
        transactions.add(transaction("2", 15000, "2023-01-02", "PENDING"));
        transactions.add(transaction("3", 2000, "2023-01-03", "COMPLETED"));

        processor.processTransactions(transactions);

        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getId() + " Status: " + transaction.getStatus());
        }
    }

    private Transaction transaction(String id, double amount, String date, String status) {
        return new com.bank.transactions.v1.Transaction(id, amount, date, status);
    }
}
