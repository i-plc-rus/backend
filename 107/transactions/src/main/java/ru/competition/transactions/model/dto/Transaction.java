
package ru.competition.transactions.model.dto;

import ru.competition.transactions.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import static ru.competition.transactions.utils.CommonConstants.INT_ZERO;

public class Transaction implements Comparable<Transaction> {
    private Long id;
    private BigDecimal amount;
    private ZonedDateTime date;
    private TransactionStatus status;

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.date = builder.date;
        this.status = builder.status;
    }

    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Transaction other)) {
            return false;
        }

        return (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    public Long getId() {
        return this.id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public boolean isLarge(BigDecimal largeAmount) {
        return this.amount != null && largeAmount != null && this.amount.compareTo(largeAmount) > INT_ZERO;
    }

    public static class Builder {
        private Long id;
        private BigDecimal amount;
        private ZonedDateTime date;
        private TransactionStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder date(ZonedDateTime date) {
            this.date = date;
            return this;
        }

        public Builder status(TransactionStatus status) {
            this.status = status;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
