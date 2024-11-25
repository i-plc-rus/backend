package com.bank.util;

import com.bank.model.TransactionStatus;
import com.bank.schema.TransactionRecord;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DataProvider {
    public static TransactionRecord getRecord() {
        return new TransactionRecord(
                UUID.randomUUID(),
                1000d,
                OffsetDateTime.now(),
                TransactionStatus.PENDING
        );
    }
}
