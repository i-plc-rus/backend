package org.example.catalogue.management.beautiful.code.afal;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionTest {

    @Test
    void test_isPending_statusPending() {
        Transaction transaction = getTransaction(BigDecimal.ZERO, TransactionStatus.PENDING);

        assertTrue(transaction.isPending());
    }

    private static Transaction getTransaction(BigDecimal zero, TransactionStatus completed) {
        return new Transaction("1", zero, LocalDate.parse("2024-10-26"), completed);
    }

    @Test
    void test_isPending_statusNotPending() {
        Transaction transaction = getTransaction(BigDecimal.ZERO, TransactionStatus.COMPLETED);

        assertFalse(transaction.isPending());
    }

    @Test
    void test_isLarge_greaterThan10000() {
        Transaction transaction = getTransaction(BigDecimal.valueOf(11000), TransactionStatus.COMPLETED);

        assertTrue(transaction.isLarge());
    }

    @Test
    void test_isLarge_lessThan10000() {
        Transaction transaction = getTransaction(BigDecimal.valueOf(9000), TransactionStatus.COMPLETED);

        assertFalse(transaction.isLarge());
    }

    @Test
    void test_isLarge_equalTo10000() {
        Transaction transaction = getTransaction(BigDecimal.valueOf(10000), TransactionStatus.COMPLETED);

        assertFalse(transaction.isLarge());
    }

    @Test
    void test_makeProcessed() {
        Transaction transaction = getTransaction(BigDecimal.ZERO, TransactionStatus.COMPLETED);

        Transaction newTransaction = transaction.makeProcessed();

        assertEquals(transaction.id(), newTransaction.id());
        assertEquals(TransactionStatus.PROCESSED, newTransaction.status());
        assertEquals(transaction.date(), newTransaction.date());
        assertEquals(transaction.amount(), newTransaction.amount());
    }
}
