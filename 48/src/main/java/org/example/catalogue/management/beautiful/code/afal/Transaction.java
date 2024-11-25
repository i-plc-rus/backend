package org.example.catalogue.management.beautiful.code.afal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(String id, BigDecimal amount, LocalDate date, TransactionStatus status) {

    private static final BigDecimal TEN_THOUSANDS = BigDecimal.valueOf(10_000);

    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }

    public boolean isLarge() {
        return amount.compareTo(TEN_THOUSANDS) > 0;
    }

    public Transaction makeProcessed() {
        return new Transaction(id, amount, date, TransactionStatus.PROCESSED);
    }
}
