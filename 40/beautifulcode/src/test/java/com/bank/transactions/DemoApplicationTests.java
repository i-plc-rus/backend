package com.bank.transactions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TransactionProcessor processor;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setupTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        LocalDate now = LocalDate.now();

        transactions.add(new Transaction(1L, BigDecimal.valueOf(5000), now, TransactionStatus.PENDING));
        transactions.add(new Transaction(1L, BigDecimal.valueOf(5000), now, TransactionStatus.PENDING));
        transactions.add(new Transaction(2L, BigDecimal.valueOf(15000), now, TransactionStatus.PROCESSED));
        transactions.add(new Transaction(3L, BigDecimal.valueOf(2000), now, TransactionStatus.COMPLETED));
        transactions.add(new Transaction(4L, BigDecimal.valueOf(5000), now, TransactionStatus.PENDING));
        transactions.add(new Transaction(5L, BigDecimal.valueOf(5000), now, TransactionStatus.PENDING));
        processor.processTransactions(transactions);
    }

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "ApplicationContext is null");
        assertNotNull(applicationContext.getBean(TransactionProcessor.class), "TransactionProcessor not available");
        assertNotNull(applicationContext.getBean(TransactionRepository.class), "TransactionRepository not available");
    }

    @Test
    void testKeyDuplication() {
        assertEquals(3, transactionRepository.getTransactions().size(), "There should be only 1 transaction");
    }

    @Test
    void testTransactionId() {
        Transaction transaction = transactionRepository.getTransaction(1L);
        assertNotNull(transaction, "There should be a transaction");
        assertEquals(1L, transaction.getId(), "There should be transaction with ID: 1");
    }

    @Test
    void testTransactionStatus() {
        Transaction transaction = transactionRepository.getTransaction(1L);
        assertNotNull(transaction, "There should be a transaction");
        assertEquals(TransactionStatus.PROCESSED, transaction.getStatus(), "The status should be: PROCESSED");
    }

    @Test
    void testGetTransactions() {
        List<Transaction> transactionList = transactionRepository.getTransactions();
        assertEquals(3, transactionList.size(), "There should be three transactions");
    }


}
