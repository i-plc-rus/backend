package com.example.sbertech.pojo;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Primary transaction class.
 */
@AllArgsConstructor
public class TransactionMine implements Transaction {

    private final UUID id;
    private final Long amountInPenny;
    private final LocalDateTime date;
    private Status status;

    @Override
    public String getId() {
        return id.toString();
    }

    @Override
    public Long getAmountInPenny() {
        return amountInPenny;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isPending() {
        return status.equals(Status.PENDING);
    }
}
