package com.bank.transactions.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionsTest {

    @Test
    public void processedTest() {
        Transactions transactions = new Transactions(List.of(
                new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PENDING),
                new Transaction("2", 11.4d, Instant.now(), TransactionStatus.PENDING),
                new Transaction("3", 12.4d, Instant.now(), TransactionStatus.COMPLETED)
        ));
        List<Transaction> processed = transactions.processed();
        assertThat(processed)
                .hasSize(2)
                .extracting(Transaction::getId).containsOnly("1", "2");
    }

    @Test
    public void noProcessedTest() {
        Transactions transactions = new Transactions(List.of(
                new Transaction("1", 10.4d, Instant.now(), TransactionStatus.COMPLETED),
                new Transaction("2", 11.4d, Instant.now(), TransactionStatus.COMPLETED),
                new Transaction("3", 12.4d, Instant.now(), TransactionStatus.COMPLETED)
        ));
        List<Transaction> processed = transactions.processed();
        assertThat(processed).isEmpty();
    }

    @Test
    public void largeTransactionIdsTest() {
        Transactions transactions = new Transactions(List.of(
                new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PENDING),
                new Transaction("2", 11.4d, Instant.now(), TransactionStatus.PENDING),
                new Transaction("3", 12.4d, Instant.now(), TransactionStatus.COMPLETED)
        ));

        List<String> ids = transactions.largeTransactionIds(11);
        assertThat(ids).containsOnly("2", "3");
    }

    @Test
    public void noLargeTransactionIdsTest() {
        Transactions transactions = new Transactions(List.of(
                new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PENDING),
                new Transaction("2", 11.4d, Instant.now(), TransactionStatus.PENDING),
                new Transaction("3", 12.4d, Instant.now(), TransactionStatus.COMPLETED)
        ));

        List<String> ids = transactions.largeTransactionIds(20);
        assertThat(ids).isEmpty();
    }
}