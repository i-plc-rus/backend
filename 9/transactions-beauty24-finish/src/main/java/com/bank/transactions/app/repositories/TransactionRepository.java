package com.bank.transactions.app.repositories;

import com.bank.transactions.app.models.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс репозитория для управления транзакциями
 * Этот класс предоставляет методы для сохранения и получения транзакций
 */
@Repository
public class TransactionRepository implements TransactionRepositoryInterface {

    private final Map<String, Transaction> transactions = new HashMap<>();

    /**
     * Сохранение определенной транзакции в репозитории
     * Если транзакция уникальна, она будет сохранена
     *
     * @param transaction транзакция для сохранения
     */
    @Override
    public void saveTransaction(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    /**
     * Получение всех транзакций из репозитория
     *
     * @return список всех транзакций
     */
    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions.values());
    }

    /**
     * Удаление всех транзакций из репозитория
     * Этот метод удаляет все транзакции из репозитория
     */
    public void clearTransactions() {
        transactions.clear();
    }
}
