package com.bank.transactions.app;
import com.bank.transactions.app.models.Transaction;
import com.bank.transactions.app.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
class TransactionRepositoryTest {

    private TransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TransactionRepository();
    }

    @Test
    void testSaveTransaction() {
        Transaction transaction = new Transaction("1", 100.0, "2023-10-01", "PENDING");
        repository.saveTransaction(transaction);
        List<Transaction> transactions = repository.getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testGetTransactions() {
        Transaction transaction1 = new Transaction("1", 100.0, "2023-10-01", "PENDING");
        Transaction transaction2 = new Transaction("2", 200.0, "2023-10-02", "PENDING");
        repository.saveTransaction(transaction1);
        repository.saveTransaction(transaction2);
        List<Transaction> transactions = repository.getTransactions();
        assertEquals(2, transactions.size());
    }

    @Test
    void testClearTransactions() {
        Transaction transaction = new Transaction("1", 100.0, "2023-10-01", "PENDING");
        repository.saveTransaction(transaction);
        repository.clearTransactions();
        assertTrue(repository.getTransactions().isEmpty());
    }
}

