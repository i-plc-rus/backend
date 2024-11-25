package com.bank.dao;

import com.bank.model.Transaction;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO-объект для сохранения транзакций.
 * Используется вместо паттерна с репозиторием для большей прозрачности в многопоточной обработке.
 */
@Component
@RequiredArgsConstructor
public class TransactionDao {
    private final EntityManager entityManager;

    public void saveTransactions(List<Transaction> records) {
        records.forEach(entityManager::merge);
    }
}
