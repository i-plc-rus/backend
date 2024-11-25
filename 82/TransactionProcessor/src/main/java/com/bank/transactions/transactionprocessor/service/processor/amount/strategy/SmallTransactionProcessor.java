package com.bank.transactions.transactionprocessor.service.processor.amount.strategy;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.service.TransactionService;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import com.bank.transactions.transactionprocessor.service.processor.TransactionProcessorStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmallTransactionProcessor implements TransactionProcessorStrategy {

    private final TransactionService transactionService;

    @Override
    public void processTransaction(TransactionDto transaction) {
        log.info("Processing small transaction: {} with amount {}", transaction.id(), transaction.amount());

        try {
            /*
                Add business logic here
             */
            transactionService.save(transaction, TransactionStatus.PROCESSED, getTransactionType());
        } catch (Exception e) {
            log.error("Error processing small transaction: {} with amount {}", transaction.id(), transaction.amount(), e);
            transactionService.save(transaction, TransactionStatus.FAILED, getTransactionType());
        }
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SMALL;
    }
}
