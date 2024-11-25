package com.bank.transactions.domain;

import com.bank.transactions.exception.TransactionParsingException;
import com.bank.transactions.exception.WrongTransactionStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    void isLargeTest() {
        Transaction transaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.COMPLETED);

        boolean isLarge = transaction.isLarge(10);
        boolean isNotLarge = !transaction.isLarge(11);

        assertThat(isLarge).isTrue();
        assertThat(isNotLarge).isTrue();
    }

    @Test
    void isPendingTest() {
        Transaction pendingTransaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PENDING);
        Transaction processedTransaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PROCESSED);

        boolean isPending = pendingTransaction.isPending();
        boolean isNotPending = !processedTransaction.isPending();

        assertThat(isPending).isTrue();
        assertThat(isNotPending).isTrue();
    }

    @Test
    void isProcessedTest() {
        Transaction processedTransaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PROCESSED);
        Transaction pendingTransaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PENDING);

        boolean isProcessed = processedTransaction.isProcessed();
        boolean isNotProcessed = !pendingTransaction.isProcessed();

        assertThat(isProcessed).isTrue();
        assertThat(isNotProcessed).isTrue();
    }

    @Test
    void processTest() {
        Transaction pendingTransaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.PENDING);
        Transaction processed = pendingTransaction.process();
        assertThat(processed.isProcessed()).isTrue();
    }

    @Test
    void failProcessTest() {
        Transaction completedTransaction = new Transaction("1", 10.4d, Instant.now(), TransactionStatus.COMPLETED);
        assertThatThrownBy(completedTransaction::process)
                .isInstanceOf(WrongTransactionStatus.class)
                .hasMessage("Transaction should be at PENDING status, but it is at COMPLETED");
    }

    @Test
    void fromString() throws TransactionParsingException {
        String s = "101 123.123 1728388938966 1";
        Transaction actual = Transaction.fromString(s);
        Transaction expected = new Transaction(
                "101", 123.123d, Instant.ofEpochMilli(1728388938966L), TransactionStatus.PENDING
        );
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}