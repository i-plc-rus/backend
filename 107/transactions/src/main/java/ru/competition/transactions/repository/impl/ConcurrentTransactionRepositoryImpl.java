
package ru.competition.transactions.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.repository.TransactionRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.competition.transactions.utils.CommonConstants.INT_ZERO;

@Component
public class ConcurrentTransactionRepositoryImpl implements TransactionRepository {
    private static final Logger LOGGER = LogManager.getLogger(ConcurrentTransactionRepositoryImpl.class.getName());
    private final Map<Long, Transaction> transactionsMap = new ConcurrentHashMap<>();

    @Override
    public void updateTransaction(Transaction transaction) {
        var key = transaction.getId();

        if (transactionsMap.containsKey(key) && transactionsMap.get(key).compareTo(transaction) >= INT_ZERO) {
            LOGGER.debug("Transaction with id: {} exist with later date", transaction.getId());
            return;
        }

        transactionsMap.put(transaction.getId(), transaction);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionsMap.values()
                .stream()
                .toList();
    }

    @Override
    public void deleteAll(ProcessorType processorType) {
        transactionsMap.clear();
    }

    @Override
    public boolean isApplicable(ProcessorType processorType) {
        return ProcessorType.CONCURRENT == processorType;
    }
}
