package ru.competition.transactions.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.TransactionProcessor;
import ru.competition.transactions.service.TransactionService;

import java.util.List;

import static ru.competition.transactions.model.enums.ProcessorType.CONCURRENT;

@Service
public class ConcurrentTransactionProcessorImpl implements TransactionProcessor {
    private final TransactionService transactionService;

    @Autowired
    public ConcurrentTransactionProcessorImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        transactions.parallelStream()
                .forEach(transaction -> transactionService.processTransaction(transaction, CONCURRENT));
    }

    @Override
    public boolean isApplicable(ProcessorType processorType) {
        return CONCURRENT == processorType;
    }

    @Override
    public void close() throws Exception {
        transactionService.deleteAll(CONCURRENT);
    }
}
