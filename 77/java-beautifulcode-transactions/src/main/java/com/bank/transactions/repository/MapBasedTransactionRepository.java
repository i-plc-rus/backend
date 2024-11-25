
package com.bank.transactions.repository;

import com.bank.transactions.annotation.AmountAuditable;
import com.bank.transactions.annotation.Loggable;
import com.bank.transactions.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository("MapBasedRepository")
public class MapBasedTransactionRepository implements TransactionRepository {
    private final ConcurrentHashMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    @Loggable
    @AmountAuditable
    public void save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public int size() {
        return transactions.size();
    }
}
