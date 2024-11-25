package com.bank.transactions.v2;

public class TransactionStatus {

    /**
     * Транзакция ожидает обработки
     */
    public static final String PENDING = "PENDING";

    /**
     * Транзакция обработана
     */
    public static final String PROCESSED = "PROCESSED";

    /**
     * Транзакция завершена
     */
    public static final String COMPLETED = "COMPLETED";

}
