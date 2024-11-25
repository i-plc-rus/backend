package com.example.sbertech.pojo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {
    @Test
    void gettersTests() {
        UUID uuid = UUID.randomUUID();
        Long amount = Long.valueOf(1);
        TransactionMine transactionMine = new TransactionMine(uuid,
                amount,
                LocalDateTime.now(),
                Status.PROCESSED);
        assertEquals(uuid.toString(), transactionMine.getId());
        assertEquals(amount, transactionMine.getAmountInPenny());
    }
}