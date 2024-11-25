package com.bank.transactions.v2;

import com.bank.transactions.Transaction;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Хранилище транзакций
 */
public class TransactionRepository {

    private static final String NULL_KEY = UUID.randomUUID().toString();

    private final Map<String, Transaction> transactions = new ConcurrentSkipListMap<>();

    /**
     * Обновляет транзакцию в хранилище
     *
     * @param transaction транзакция
     */
    public void updateTransaction(Transaction transaction) {
        var key = transaction.getId() != null ? transaction.getId() : NULL_KEY;
        transactions.put(key, transaction);
    }

    /**
     * Возвращает все транзакции, имеющиеся в хранилище
     *
     * @return транзакции
     */
    public List<Transaction> getTransactions() {
        return transactions.values().stream().toList();
    }

    /**
     * Очищает хранилище
     */
    public void deleteAll() {
        transactions.clear();
    }
}
