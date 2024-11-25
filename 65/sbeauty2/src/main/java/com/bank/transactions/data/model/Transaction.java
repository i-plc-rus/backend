package com.bank.transactions.data.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static com.bank.transactions.data.model.TransactionStatus.PENDING;
import static com.bank.transactions.data.model.TransactionStatus.PROCESSED;

public class Transaction {
    private static final int LARGE_TRANSACTION_AMOUNT_MIN = 10_000;
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    private final String id;
    private final BigDecimal amount;
    private final LocalDate date;
    private TransactionStatus status;

    public Transaction(String id, BigDecimal amount, LocalDate date, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public boolean isPending() {
        return PENDING == getStatus();
    }

    public boolean isProcessed() {
        return PROCESSED == getStatus();
    }

    public boolean hasLargeAmount() {
        return getAmount().intValue() > LARGE_TRANSACTION_AMOUNT_MIN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        MDC.put(MDCKey.TRANSACTION_ID.getKeyName(), getId());
//        try {
//            logger.warn("Finalize transaction");
//        } finally {
//            MDC.remove(MDCKey.TRANSACTION_ID.getKeyName());
//        }
//    }
}
