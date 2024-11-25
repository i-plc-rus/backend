package ru.competition.transactions.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.model.enums.TransactionStatus;
import ru.competition.transactions.repository.TransactionRepositoryFactory;
import ru.competition.transactions.service.TransactionService;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionServiceImpl.class.getName());
    private final TransactionRepositoryFactory repositoryFactory;
    @Value("${transactions.large-amount}")
    private BigDecimal largeAmount;

    @Autowired
    public TransactionServiceImpl(TransactionRepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void processTransaction(Transaction transaction, ProcessorType processorType) {
        if (transaction.isLarge(largeAmount)) {
            LOGGER.info("Processing large transaction, id: {}", transaction.getId());
        }

        try {
            if (transaction.getStatus() == TransactionStatus.PENDING) {
                transaction.setStatus(TransactionStatus.PROCESSED);
                repositoryFactory.getTransactionRepository(processorType).updateTransaction(transaction);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing transaction: {}", e.getMessage());
        }
    }

    @Override
    public void deleteAll(ProcessorType processorType) {
        repositoryFactory.getTransactionRepository(processorType).deleteAll(processorType);
    }
}
