package com.bank.transactions.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Data
@EqualsAndHashCode(of = {"id"})
public class Transaction {
    // поддержка многопоточности при создании
    private static AtomicLong counter = new AtomicLong(0);

    private final Long id;
    private final BigDecimal amount;        // корректность математических операций
    private final Instant date;             // экономия памяти, коректная сортировка
    private Status status;                  // исключение недопустимых значений

    public Transaction(BigDecimal amount) {
        this.id = counter.incrementAndGet();
        this.amount = amount;
        this.date = Instant.now();
        this.status = Status.PENDING;
    }

    public enum Status {
        PENDING,
        PROCESSED
    }
}
