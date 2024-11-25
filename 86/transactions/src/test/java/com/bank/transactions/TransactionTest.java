package com.bank.transactions;

import org.junit.jupiter.api.Test;

import static com.bank.transactions.TransactionStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("resource")
class TransactionTest {

    @Test
    public void shouldMarkProcessedWhenProcess() {

        Transaction transaction = new Transaction("1", 1000);

        assertEquals(PENDING, transaction.getStatus());

        transaction.process();

        assertEquals(PROCESSED, transaction.getStatus());
    }

    @Test
    public void shouldReturnTrueCauseStatusIsPending() {

        Transaction transaction = new Transaction("1", 1000);

        assertEquals(PENDING, transaction.getStatus());
        assertTrue(transaction.isPending());
    }

    @Test
    public void shouldMarkCompletedWhenClose() {
        Transaction transaction = new Transaction("1", 1000);

        assertEquals(PENDING, transaction.getStatus());

        transaction.close();

        assertEquals(COMPLETED, transaction.getStatus());
    }
}