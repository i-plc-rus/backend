package ru.competition.transactions.service;

import ru.competition.transactions.model.enums.ProcessorType;

public interface TransactionProcessorFactory {

    TransactionProcessor getTransactionProcessor(ProcessorType processorType);
}
