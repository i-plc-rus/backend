package com.example.sbertech.pojo;

/**
 * Interface for transaction objects.
 */
public interface Transaction {

    /**
     * Transaction ID.
     *
     * @return ID. When junior implementation is removed, change this type to UUID.
     */
    Object getId();

    /**
     * Use amount in penny to avoid double type rounding error.
     * Use Long instead of long to avoid rare not-atomic write.
     *
     * @return transaction amount in penny.
     */
    Long getAmountInPenny();

    /**
     * Set new status.
     *
     * @param status enum.
     */
    void setStatus(Status status);

    /**
     * Get status.
     *
     * @return Status enum.
     */
    boolean isPending();
}
