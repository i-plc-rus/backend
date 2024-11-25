package org.example.catalogue.management.beautiful.code.afal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TransactionProcessorRepositoryMockTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionProcessor processor;

    @Test
    public void test_nullInput() {
        Assertions.assertDoesNotThrow(() -> processor.submitTransactions(null));
        verify(transactionRepository, never()).updateTransaction(any());
    }

    @Test
    public void test_emptyListInput() {
        List<Transaction> transactions = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> processor.submitTransactions(transactions));
        verify(transactionRepository, never()).updateTransaction(any());
    }

    @Test
    public void test_lotsOfLargeTransactions() {
        List<Transaction> transactions = IntStream.range(0, 10)
            .mapToObj(i -> new Transaction(String.valueOf(i), BigDecimal.valueOf(20000L + i * 1000L),
                LocalDate.parse("2023-01-01"), TransactionStatus.PENDING
            ))
            .collect(Collectors.toList());

        processor.submitTransactions(transactions);

        Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> verify(transactionRepository, times(10)).updateTransaction(any()));

        for (Transaction transaction : transactions) {
            verify(transactionRepository, times(1)).updateTransaction(
                argThat(t -> t.id().equals(transaction.id()) && t.status() == TransactionStatus.PROCESSED));
        }
    }

    @Test
    public void test_mixedTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("small1", BigDecimal.valueOf(2000), LocalDate.parse("2023-01-01"),
            TransactionStatus.PENDING
        ));
        transactions.add(new Transaction("small2", BigDecimal.valueOf(3000), LocalDate.parse("2023-01-02"),
            TransactionStatus.PENDING
        ));
        transactions.add(new Transaction("small3", BigDecimal.valueOf(1500), LocalDate.parse("2023-01-03"),
            TransactionStatus.PENDING
        ));

        for (int i = 0; i < 6; i++) {
            transactions.add(
                new Transaction("large" + i, BigDecimal.valueOf(20000L + i * 1000L), LocalDate.parse("2023-01-04"),
                    TransactionStatus.PENDING
                ));
        }

        processor.submitTransactions(transactions);

        Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> verify(transactionRepository, times(9)).updateTransaction(any()));

        for (Transaction transaction : transactions) {
            verify(transactionRepository, times(1)).updateTransaction(
                argThat(t -> t.id().equals(transaction.id()) && t.status() == TransactionStatus.PROCESSED));
        }
    }

    @Test
    public void test_onlyCompletedTransactions() {
        List<Transaction> transactions = IntStream.range(0, 5)
            .mapToObj(i -> new Transaction(String.valueOf(i), BigDecimal.valueOf(5000L + i * 1000L),
                LocalDate.parse("2023-01-01"), TransactionStatus.COMPLETED
            ))
            .collect(Collectors.toList());

        processor.submitTransactions(transactions);

        Awaitility.await()
            .atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() -> verify(transactionRepository, never()).updateTransaction(any()));
    }

    @Test
    public void test_submitTransactionsWhenRepositoryThrowsException() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(
            new Transaction("1", BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), TransactionStatus.PENDING));
        transactions.add(
            new Transaction("2", BigDecimal.valueOf(15000), LocalDate.parse("2023-01-02"), TransactionStatus.PENDING));
        transactions.add(
            new Transaction("3", BigDecimal.valueOf(2000), LocalDate.parse("2023-01-03"), TransactionStatus.PENDING));

        doThrow(new RuntimeException("Database error")).when(transactionRepository)
            .updateTransaction(argThat(transaction -> transaction.id().equals("2")));

        processor.submitTransactions(transactions);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(transactionRepository, times(1)).updateTransaction(
                argThat(t -> t.id().equals("1") && t.status() == TransactionStatus.PROCESSED));
            verify(transactionRepository, times(3)).updateTransaction(
                argThat(t -> t.id().equals("2") && t.status() == TransactionStatus.PROCESSED));
            verify(transactionRepository, times(1)).updateTransaction(
                argThat(t -> t.id().equals("3") && t.status() == TransactionStatus.PROCESSED));
        });
    }
}
