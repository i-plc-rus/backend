package com.bank.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor processor;

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("1", 5000, "2023-01-01", "PENDING"),
                new Transaction("2", 15000, "2023-01-02", "PENDING"),
                new Transaction("3", 2000, "2023-01-03", "COMPLETED")
        );

        processor.processTransactions(transactions);

        assertEquals("PROCESSED", transactions.get(0).getStatus(), "Transaction 1 should be PROCESSED");
        assertEquals("PROCESSED", transactions.get(1).getStatus(), "Transaction 2 should be PROCESSED");
        assertEquals("COMPLETED", transactions.get(2).getStatus(), "Transaction 3 should remain COMPLETED");
    }
}