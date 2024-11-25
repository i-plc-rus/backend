package ru.competition.transactions.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.TransactionFacade;
import ru.competition.transactions.service.TransactionProcessor;
import ru.competition.transactions.service.TransactionProcessorFactory;
import ru.competition.transactions.service.TransactionValidationService;

import java.util.List;

@Service
public class TransactionFacadeImpl implements TransactionFacade {
    private static final Logger LOGGER = LogManager.getLogger(TransactionFacadeImpl.class.getName());
    private final TransactionProcessorFactory processorFactory;
    private final TransactionValidationService validationService;

    @Autowired
    public TransactionFacadeImpl(TransactionProcessorFactory processorFactory,
                                 TransactionValidationService validationService) {
        this.processorFactory = processorFactory;
        this.validationService = validationService;
    }

    @Override
    public void process(List<Transaction> transactions, ProcessorType processorType) {
        try (final TransactionProcessor transactionProcessor = processorFactory.getTransactionProcessor(processorType)) {
            List<Transaction> validatedTransactions = validationService.validate(transactions);
            transactionProcessor.processTransactions(validatedTransactions);
        } catch (Exception e) {
            LOGGER.error("Error processing: {}", e.getMessage());
        }
    }
}