package ru.competition.transactions.service;

import ru.competition.transactions.model.dto.Transaction;

import java.util.List;

public interface TransactionProcessor extends ProcessorStrategy, AutoCloseable {

    void processTransactions(List<Transaction> transactions);
}
