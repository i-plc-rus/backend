package ru.competition.transactions.repository;

import ru.competition.transactions.model.enums.ProcessorType;

public interface TransactionRepositoryFactory {

    TransactionRepository getTransactionRepository(ProcessorType processorType);
}
