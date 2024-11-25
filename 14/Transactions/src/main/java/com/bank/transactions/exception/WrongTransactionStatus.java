package com.bank.transactions.exception;

import com.bank.transactions.domain.TransactionStatus;

public class WrongTransactionStatus extends RuntimeException {
    public WrongTransactionStatus(TransactionStatus expectedStatus, TransactionStatus actualStatus) {
        super(String.format("Transaction should be at %s status, but it is at %s", expectedStatus, actualStatus));
    }
}
