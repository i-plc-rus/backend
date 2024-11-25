package com.bank.transactions;

import com.bank.transactions.model.Transaction;
import com.bank.transactions.model.TransactionStatus;
import com.bank.transactions.service.TransactionProcessor;
import com.bank.transactions.service.TransactionService;
import com.bank.transactions.util.TransactionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TransactionProcessorTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionFactory transactionFactory;

    private TransactionProcessor transactionProcessor;

    @BeforeEach
    void setUp() {
        transactionProcessor = new TransactionProcessor(transactionService, transactionFactory);
    }

    @Test
    void processTransactionsWithValidTransactionsShouldProcessAllTransactionsTest() {
        List<Transaction> transactions = createTransactionList(5, 1000);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(5)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithEmptyListShouldNotProcessAnyTransactionsTest() {
        List<Transaction> transactions = new ArrayList<>();

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.never()).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithLargeTransactionShouldProcessTransactionTest() {
        List<Transaction> transactions = createTransactionList(1, 15000);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(1)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithNullTransactionShouldNotProcessAnyTransactionsTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(null);

        Assertions.assertDoesNotThrow(() -> transactionProcessor.processTransactions(transactions));
        Mockito.verify(transactionService, Mockito.never()).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithHighVolumeTransactionsShouldCompleteWithinTimeLimitTest() {
        List<Transaction> transactions = createTransactionList(10000, 1000);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        long startTime = System.nanoTime();
        transactionProcessor.processTransactions(transactions);
        long endTime = System.nanoTime();

        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        Assertions.assertTrue(duration < 5000, "Processing 10000 transactions took more than 5 seconds");

        Mockito.verify(transactionService, Mockito.times(10000)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithNullListShouldNotProcessAnyTransactionsTest() {
        Assertions.assertDoesNotThrow(() -> transactionProcessor.processTransactions(null));
        Mockito.verify(transactionService, Mockito.never()).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithMixedValidAndNullTransactionsShouldProcessOnlyValidTransactionsTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("id1", 1000, LocalDateTime.now(), TransactionStatus.PENDING));
        transactions.add(null);
        transactions.add(new Transaction("id2", 2000, LocalDateTime.now(), TransactionStatus.PENDING));

        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(2)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithAllNullTransactionsShouldNotProcessAnyTransactionsTest() {
        List<Transaction> transactions = Arrays.asList(null, null, null);

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.never()).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithNegativeAmountShouldProcessTransactionTest() {
        List<Transaction> transactions = createTransactionList(1, -1000);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(1)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithZeroAmountShouldProcessTransactionTest() {
        List<Transaction> transactions = createTransactionList(1, 0);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(1)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithVeryLargeAmountShouldProcessTransactionAndVerifyCreationTest() {
        List<Transaction> transactions = createTransactionList(1, Double.MAX_VALUE);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(1)).processTransaction(Mockito.any(Transaction.class));
        Mockito.verify(transactionFactory, Mockito.times(1)).createTransaction(ArgumentMatchers.eq(Double.MAX_VALUE), Mockito.any(LocalDateTime.class));
    }

    @Test
    void processTransactionsWithDifferentTransactionStatusesShouldProcessAllTransactionsTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("id1", 1000, LocalDateTime.now(), TransactionStatus.PENDING));
        transactions.add(new Transaction("id2", 2000, LocalDateTime.now(), TransactionStatus.PROCESSED));
        transactions.add(new Transaction("id3", 3000, LocalDateTime.now(), TransactionStatus.FAILED));

        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(3)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWhenExceptionThrownInTransactionServiceShouldHandleGracefullyTest() {
        List<Transaction> transactions = createTransactionList(1, 1000);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));
        Mockito.doThrow(new RuntimeException("Processing error")).when(transactionService).processTransaction(Mockito.any(Transaction.class));

        Assertions.assertDoesNotThrow(() -> transactionProcessor.processTransactions(transactions));
        Mockito.verify(transactionService, Mockito.times(1)).processTransaction(Mockito.any(Transaction.class));
    }

    @Test
    void processTransactionsWithConcurrentExecutionShouldProcessAllTransactionsTest() {
        int transactionCount = 1000;
        List<Transaction> transactions = createTransactionList(transactionCount, 1000);
        Mockito.when(transactionFactory.createTransaction(Mockito.anyDouble(), Mockito.any(LocalDateTime.class)))
                .thenAnswer(invocation -> new Transaction("newId", invocation.getArgument(0), invocation.getArgument(1), TransactionStatus.PENDING));

        transactionProcessor.processTransactions(transactions);

        Mockito.verify(transactionService, Mockito.times(transactionCount)).processTransaction(Mockito.any(Transaction.class));
    }

    private List<Transaction> createTransactionList(int count, double amount) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            transactions.add(new Transaction("id" + i, amount, LocalDateTime.now(), TransactionStatus.PENDING));
        }
        return transactions;
    }
}
