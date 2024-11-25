package com.bank.transactions.transactionprocessor.repository;

import com.bank.transactions.transactionprocessor.repository.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionInMemoryRepository implements TransactionRepository {

    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        transactions.remove(id);
    }

    @Override
    public int count() {
        return transactions.values().size();
    }

    @Override
    public void clear() {
        transactions.clear();
    }
}
