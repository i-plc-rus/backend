package com.bank.transactions.app.repositories;

import com.bank.transactions.app.models.Transaction;

import java.util.List;

/**
 * Интерфейс репозитория для управления транзакциями
 * Этот интерфейс предоставляет методы для сохранения и получения транзакций
 */
public interface TransactionRepositoryInterface {

    /**
     * Сохранение определенной транзакции в репозитории
     *
     * @param transaction транзакция для сохранения
     */
    void saveTransaction(Transaction transaction);

    /**
     * Получение всех транзакций из репозитория
     *
     * @return список всех транзакций
     */
    List<Transaction> getTransactions();
}

