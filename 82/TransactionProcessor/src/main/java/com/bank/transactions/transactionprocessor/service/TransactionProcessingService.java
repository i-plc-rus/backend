package com.bank.transactions.transactionprocessor.service;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class TransactionProcessingService {

    private final TransactionProcessor transactionProcessor;

    public void processTransactions(@Valid List<TransactionDto> transactions) {
        for (TransactionDto transaction : transactions) {
            transactionProcessor.processTransaction(transaction);
        }
    }
}