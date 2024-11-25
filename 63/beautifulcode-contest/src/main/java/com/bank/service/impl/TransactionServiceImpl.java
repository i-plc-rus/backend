package com.bank.service.impl;

import com.bank.exception.ExceptionMessage;
import com.bank.mapper.ExceptionMapper;
import com.bank.processor.TransactionProcessor;
import com.bank.schema.TransactionRecord;
import com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionProcessor transactionProcessor;
    private final ExceptionMapper exceptionMapper;

    @Override
    public List<ExceptionMessage> processTransaction(List<TransactionRecord> transactions) {
        return transactionProcessor.processTransactions(transactions).stream()
                .map(exceptionMapper::toExceptionMessage)
                .toList();
    }
}
