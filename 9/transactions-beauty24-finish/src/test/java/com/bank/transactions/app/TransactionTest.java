package com.bank.transactions.app;

import com.bank.transactions.app.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionTest {

    @Test
    void testTransactionCreation() {
        Transaction transaction = new Transaction("1", 100.0, "2023-10-01", "PENDING");
        assertEquals("1", transaction.getId());
        assertEquals(100.0, transaction.getAmount());
        assertEquals("2023-10-01", transaction.getDate());
        assertEquals("PENDING", transaction.getStatus());
    }

    @Test
    void testSetStatus() {
        Transaction transaction = new Transaction("1", 100.0, "2023-10-01", "PENDING");
        transaction.setStatus("PROCESSED");
        assertEquals("PROCESSED", transaction.getStatus());
    }
}
