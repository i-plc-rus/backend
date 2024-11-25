package com.bank.transactions.transactionprocessor.service.processor;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionType;

public interface TransactionProcessorStrategy {

    void processTransaction(TransactionDto transaction);

    TransactionType getTransactionType();
}
