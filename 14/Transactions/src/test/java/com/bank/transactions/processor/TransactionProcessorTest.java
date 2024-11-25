package com.bank.transactions.processor;

import com.bank.transactions.BaseTest;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionProcessorTest extends BaseTest {

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = List.of(
            new Transaction("1", 5000, Instant.parse("2023-01-01T00:00:00Z"), TransactionStatus.PENDING),
            new Transaction("2", 15000, Instant.parse("2023-01-02T00:00:00Z"), TransactionStatus.PENDING),
            new Transaction("3", 2000, Instant.parse("2023-01-03T00:00:00Z"), TransactionStatus.COMPLETED)
        );

        oldProcessor.processTransactions(transactions);

        List<Transaction> actual = repository.getTransactions();
        assertThat(actual).hasSize(2);
        assertThat(actual).allMatch(Transaction::isProcessed);
    }
}