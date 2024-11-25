package ru.competition.transactions.service;

import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;

public interface TransactionService {

    void processTransaction(Transaction transaction, ProcessorType processorType);

    void deleteAll(ProcessorType processorType);
}
