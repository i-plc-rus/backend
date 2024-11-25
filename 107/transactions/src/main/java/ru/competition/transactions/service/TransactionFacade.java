package ru.competition.transactions.service;

import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;

import java.util.List;

public interface TransactionFacade {

    void process(List<Transaction> transactions, ProcessorType processorType);
}
