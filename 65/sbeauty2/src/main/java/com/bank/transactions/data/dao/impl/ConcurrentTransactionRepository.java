package com.bank.transactions.data.dao.impl;

import com.bank.transactions.data.dao.TransactionRepository;
import com.bank.transactions.data.model.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Collection;
import java.util.Map;

/**
 * Concurrent transaction repository based on synchronized hash map with soft references
 */
@Repository
@ConditionalOnProperty(prefix = "transaction.repository", name = "type", havingValue = "PARALLEL", matchIfMissing = true)
public class ConcurrentTransactionRepository implements TransactionRepository {

    private final Map<String, Transaction> transactions = new ConcurrentReferenceHashMap<>(); // default reference type - soft

    @Override
    public void updateTransaction(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public Collection<Transaction> getTransactions() {
        return transactions.values();
    }
}
