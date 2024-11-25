
package ru.competition.transactions.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.competition.transactions.utils.CommonConstants.INT_ZERO;

@Component
public class SequentialTransactionRepositoryImpl implements TransactionRepository {
    private static final Logger LOGGER = LogManager.getLogger(SequentialTransactionRepositoryImpl.class.getName());
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void updateTransaction(Transaction transaction) {
        if (transactions.contains(transaction) && existLatest(transactions, transaction)) {
            LOGGER.debug("Transaction with id: {} exist with later date", transaction.getId());
            return;
        }

        transactions.add(transaction);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void deleteAll(ProcessorType processorType) {
    }

    @Override
    public boolean isApplicable(ProcessorType processorType) {
        return ProcessorType.SEQUENTIAL == processorType;
    }

    private boolean existLatest(List<Transaction> transactions, Transaction newTransaction) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(newTransaction.getId()))
                .anyMatch(transaction -> transaction.compareTo(newTransaction) >= INT_ZERO);
    }
}
