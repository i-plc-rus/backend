package com.example.sbertech.pojo;

/**
 * Junior transaction class. Remained for profiling goal. To delete after.
 */
public class TransactionJunior implements Transaction {
    private final String id;
    private final long amountInPenny;
    private final String date;
    private Status status;

    public TransactionJunior(String id, long amountInPenny, String date, Status status) {
        this.id = id;
        this.amountInPenny = amountInPenny;
        this.date = date;
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Long getAmountInPenny() {
        return amountInPenny;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isPending() {
        return status.equals(Status.PENDING);
    }
}
