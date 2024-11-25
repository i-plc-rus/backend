package com.bank.transactions.transactionprocessor.service.processor;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionType;

public interface TransactionTypeResolver {

    TransactionType getType(TransactionDto transactionDto);
}
