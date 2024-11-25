package com.bank.transactions.app;

import com.bank.transactions.app.models.Transaction;
import com.bank.transactions.app.repositories.TransactionRepository;
import com.bank.transactions.app.services.FileLogger;
import com.bank.transactions.app.services.Logger;
import com.bank.transactions.app.services.TransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionProcessorTest {

    private TransactionRepository repository;
    private Logger logger;
    private TransactionProcessor processor;

    @BeforeEach
    public void setUp() {
        repository = mock(TransactionRepository.class);
        logger = mock(Logger.class);
        processor = new TransactionProcessor(repository, logger);
    }

    @Test
    public void testProcessTransactions_ValidTransaction() {
        Transaction transaction = new Transaction("1", 5000, "2023-10-01", "PENDING");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        processor.processTransactions(transactions);

        verify(logger).log("Transaction 1 status changed from PENDING to PROCESSED");
        verify(repository).saveTransaction(transaction);
        assertEquals("PROCESSED", transaction.getStatus());
    }

    @Test
    public void testProcessTransactions_LargeTransaction() {
        Transaction transaction = new Transaction("2", 15000, "2023-10-01", "PENDING");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        processor.processTransactions(transactions);

        verify(logger).log("Processing large transaction: 2");
        verify(repository).saveTransaction(transaction);
    }

    @Test
    public void testProcessTransactions_NegativeAmount() {
        Transaction transaction = new Transaction("3", -100, "2023-10-01", "PENDING");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        processor.processTransactions(transactions);

        verify(logger).log("Error processing transaction: Transaction amount cannot be negative");
        verify(repository, never()).saveTransaction(any());
    }

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("4", 5000, "2023-01-01", "PENDING"));
        transactions.add(new Transaction("5", 15000, "2023-01-02", "PENDING"));
        transactions.add(new Transaction("6", 2000, "2023-01-03", "COMPLETED"));

        processor.processTransactions(transactions);

        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getId() + " Status: " + transaction.getStatus());
        }
    }

    @Test
    public void testProcessTransactions_DuplicateTransactions() {
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionProcessor transactionProcessor = new TransactionProcessor(transactionRepository, logger);

        Transaction transaction1 = new Transaction("7", 100, "2023-10-01", "PENDING");
        transactionRepository.saveTransaction(transaction1);

        Transaction transaction2 = new Transaction("7", 200, "2023-10-02", "PENDING");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction2);

        transactionProcessor.processTransactions(transactions);
        verify(logger).log("Error processing transaction: Transaction ID must be unique");
    }
}
