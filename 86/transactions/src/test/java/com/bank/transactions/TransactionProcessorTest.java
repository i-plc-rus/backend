package com.bank.transactions;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static com.bank.transactions.TransactionStatus.COMPLETED;
import static com.bank.transactions.TransactionStatus.PENDING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionProcessorTest {

    @Autowired private TransactionProcessor processor;

    @MockBean private TransactionRepository repository;
    @MockBean private TransactionLogger logger;
    @MockBean private TransactionPreferences preferences;

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("1", 5000, "2023-01-01", TransactionStatus.PENDING));
        transactions.add(new Transaction("2", 15000, "2023-01-02", TransactionStatus.PROCESSED));
        transactions.add(new Transaction("3", 2000, "2023-01-03", COMPLETED));

        processor.processTransactions(transactions);

        for (Transaction transaction : transactions) {
            System.out.println("com.bank.transactions.Transaction ID: " + transaction.getId() + " Status: " + transaction.getStatus());
        }
    }

    @Test
    public void shouldNotThrowsWhenProcessingEmptyTransactionList() {

        List<Transaction> transactions = new ArrayList<>();

        assertDoesNotThrow(() -> processor.processTransactions(transactions));
    }

    @Test
    public void shouldProcessOnlyPendingTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        Transaction pendingTransaction = mock(Transaction.class);
        Transaction notPendingTransaction = mock(Transaction.class);

        transactions.add(pendingTransaction);
        transactions.add(notPendingTransaction);

        when(pendingTransaction.isPending()).thenReturn(true);
        when(notPendingTransaction.isPending()).thenReturn(false);

        processor.processTransactions(transactions);

        verify(pendingTransaction, times(1)).process();
        verify(notPendingTransaction, never()).process();
    }

    @Test
    public void shouldUpdateOnlyPendingTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        Transaction pendingTransaction = mock(Transaction.class);
        Transaction notPendingTransaction = mock(Transaction.class);

        transactions.add(pendingTransaction);

        when(pendingTransaction.isPending()).thenReturn(true);
        when(notPendingTransaction.isPending()).thenReturn(false);

        processor.processTransactions(transactions);

        verify(repository, times(1)).update(pendingTransaction);
        verify(repository, never()).update(notPendingTransaction);
    }

    @Test
    public void shouldLogOnlyLargeTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        int largeTransactionAmount = 10000;

        Transaction equalsLargeAmountTransaction = new Transaction("1", largeTransactionAmount);
        Transaction greaterThanLargeAmountTransaction = new Transaction("2", largeTransactionAmount + 100);
        Transaction lowerThanLargeAmountTransaction = new Transaction("3", largeTransactionAmount - 100);

        transactions.add(equalsLargeAmountTransaction);
        transactions.add(greaterThanLargeAmountTransaction);
        transactions.add(lowerThanLargeAmountTransaction);

        when(preferences.getLargeTransactionAmount()).thenReturn(largeTransactionAmount);

        processor.processTransactions(transactions);

        verify(logger, times(1)).info(anyString(), eq(equalsLargeAmountTransaction));
        verify(logger, times(1)).info(anyString(), eq(greaterThanLargeAmountTransaction));
        verify(logger, never()).info(anyString(), eq(lowerThanLargeAmountTransaction));
    }

    @Test
    public void shouldNotThrowsWhenTransactionProcessThrows() {

        List<Transaction> transactions = new ArrayList<>();

        Transaction pendingTransaction = mock(Transaction.class);

        transactions.add(pendingTransaction);

        when(pendingTransaction.isPending()).thenReturn(true);
        doThrow(new RuntimeException("stub exception")).when(pendingTransaction).process();

        assertDoesNotThrow(() -> processor.processTransactions(transactions));
        verify(pendingTransaction, times(1)).process();
    }

    @Test
    public void shouldMarkTransactionsAsCompletedAfterProcessTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        Transaction t1 = new Transaction("1", 1000);
        Transaction t2 = new Transaction("2", 2000);

        transactions.add(t1);
        transactions.add(t2);

        assertEquals(PENDING, t1.getStatus());
        assertEquals(PENDING, t1.getStatus());

        processor.processTransactions(transactions);

        assertEquals(COMPLETED, t1.getStatus());
        assertEquals(COMPLETED, t1.getStatus());
    }
}
