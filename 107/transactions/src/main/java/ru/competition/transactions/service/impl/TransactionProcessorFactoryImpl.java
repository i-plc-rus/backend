package ru.competition.transactions.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.TransactionProcessor;
import ru.competition.transactions.service.TransactionProcessorFactory;

import java.util.List;

@Service
public class TransactionProcessorFactoryImpl implements TransactionProcessorFactory {
    private static final String PROCESSOR_NOT_FOUND = "TransactionProcessor with type: %s not found";

    private final List<TransactionProcessor> transactionProcessors;

    @Autowired
    public TransactionProcessorFactoryImpl(List<TransactionProcessor> transactionProcessors) {
        this.transactionProcessors = transactionProcessors;
    }

    @Override
    public TransactionProcessor getTransactionProcessor(ProcessorType processorType) {
        return transactionProcessors.stream()
                .filter(processor -> processor.isApplicable(processorType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(PROCESSOR_NOT_FOUND, processorType)));
    }
}
