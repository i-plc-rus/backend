package com.bank.transactions.processor;

import com.bank.transactions.BaseTest;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionsProcessorTest extends BaseTest {

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = List.of(
                new Transaction("1", 5000, Instant.parse("2023-01-01T00:00:00Z"), TransactionStatus.PENDING),
                new Transaction("2", 15000, Instant.parse("2023-01-02T00:00:00Z"), TransactionStatus.PENDING),
                new Transaction("3", 2000, Instant.parse("2023-01-03T00:00:00Z"), TransactionStatus.COMPLETED)
        );

        newProcessor.processTransactions(transactions);

        List<Transaction> actual = repository.getTransactions();
        assertThat(actual).hasSize(2);
        assertThat(actual).allMatch(Transaction::isProcessed);
    }

    @Test
    public void testProcessZeroTransactions() {
        newProcessor.processTransactions(Collections.emptyList());

        int size = repository.getTransactions().size();
        assertThat(size).isEqualTo(0);
    }

    @Test
    public void testProcessIncorrectStatusTransactions() {
        Instant now = Instant.now();
        List<Transaction> transactions = List.of(
                new Transaction("1", 5000, now, TransactionStatus.PROCESSED),
                new Transaction("2", 15000, now, TransactionStatus.PROCESSED),
                new Transaction("3", 15000, now, TransactionStatus.PROCESSED),
                new Transaction("4", 2000, now, TransactionStatus.COMPLETED),
                new Transaction("5", 2000, now, TransactionStatus.COMPLETED)
        );
        newProcessor.processTransactions(transactions);

        int size = repository.getTransactions().size();
        assertThat(size).isEqualTo(0);
    }

    @Test
    public void testProcessTransactionsFewTimes() {
        Instant now = Instant.now();
        List<Transaction> transactions1 = List.of(
                new Transaction("1", 5000, now, TransactionStatus.PENDING),
                new Transaction("2", 15000, now, TransactionStatus.PENDING),
                new Transaction("3", 15000, now, TransactionStatus.PENDING)
        );
        List<Transaction> transactions2 = List.of(
                new Transaction("4", 2000, now, TransactionStatus.PENDING),
                new Transaction("5", 2000, now, TransactionStatus.PENDING)
        );
        newProcessor.processTransactions(transactions1);
        newProcessor.processTransactions(transactions2);

        int size = repository.getTransactions().size();
        assertThat(size).isEqualTo(5);
    }
}