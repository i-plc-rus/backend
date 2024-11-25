
package ru.competition.transactions.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.TransactionProcessor;
import ru.competition.transactions.service.TransactionService;

import java.util.List;

import static ru.competition.transactions.model.enums.ProcessorType.SEQUENTIAL;

@Service
public class SequentialTransactionProcessorImpl implements TransactionProcessor {
    private final TransactionService transactionService;

    @Autowired
    public SequentialTransactionProcessorImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        transactions.forEach(transaction -> transactionService.processTransaction(transaction, SEQUENTIAL));
    }

    @Override
    public boolean isApplicable(ProcessorType processorType) {
        return SEQUENTIAL == processorType;
    }

    @Override
    public void close() throws Exception {
        transactionService.deleteAll(SEQUENTIAL);
    }
}
