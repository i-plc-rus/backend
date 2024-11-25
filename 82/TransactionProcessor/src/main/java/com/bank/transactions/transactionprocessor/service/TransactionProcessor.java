package com.bank.transactions.transactionprocessor.service;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import com.bank.transactions.transactionprocessor.exception.ProcessorNotFoundException;
import com.bank.transactions.transactionprocessor.service.processor.TransactionProcessorStrategy;
import com.bank.transactions.transactionprocessor.service.processor.TransactionTypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionProcessor {

    private final TransactionService transactionService;

    private final TransactionTypeResolver typeResolver;

    private final Map<TransactionType, TransactionProcessorStrategy> processors;

    public TransactionProcessor(TransactionService transactionService,
                                TransactionTypeResolver typeResolver,
                                List<TransactionProcessorStrategy> strategies) {
        this.transactionService = transactionService;
        this.typeResolver = typeResolver;
        this.processors = strategies.stream()
                .collect(Collectors.toMap(
                        TransactionProcessorStrategy::getTransactionType,
                        Function.identity())
                );
    }

    @Async("transactionExecutor")
    public void processTransaction(TransactionDto transaction) {
        try {
            if (TransactionStatus.PENDING.equals(transaction.status())) {
                if (transactionService.isTransactionProcessed(transaction.id())) {
                    log.info("Transaction {} is already processed", transaction.id());
                    return;
                }

                TransactionProcessorStrategy processor = getTransactionProcessor(transaction);
                processor.processTransaction(transaction);
            } else if (TransactionStatus.COMPLETED.equals(transaction.status())) {
                log.info("Transaction {} is completed", transaction.id());
                transactionService.completeTransaction(transaction.id());
            }
        } catch (Exception e) {
            handleProcessingError(transaction, e);
            throw e;
        }
    }

    private TransactionProcessorStrategy getTransactionProcessor(TransactionDto transaction) {
        TransactionType type = typeResolver.getType(transaction);
        TransactionProcessorStrategy processor = processors.get(type);

        if (processor == null) {
            throw new ProcessorNotFoundException(type);
        }
        return processor;
    }

    private void handleProcessingError(TransactionDto transaction, Throwable e) {
        log.error("Error processing transaction: {}. Error: {}", transaction, e.getMessage());
        transactionService.saveFailedTransaction(transaction);
    }
}