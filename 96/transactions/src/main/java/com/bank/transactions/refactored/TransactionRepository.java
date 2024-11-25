package com.bank.transactions.refactored;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/// Используем [Slf4j] из Lombok для логирования
@Slf4j
@Repository
public class TransactionRepository {
    /// Параллельная карта для транзакций, должна быть эффективнее [ArrayList]. Вмещает немногим больше 1 млрд значений,
    /// но в реальном проекте для таких целей должна использоваться база данных. Начальный размер установлен на 1 млн
    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>(1 << 20);

    /// Генерирует случайный `id`, если изначально `id` у транзакции отсутствует. Если `id` установлен заранее,
    /// проверяет есть ли транзакция с таким `id` в коллекции, и если есть, то сообщает об обновлении транзакции в лог.
    /// Добавляет транзакцию в коллекцию
    public void updateTransaction(Transaction transaction) {
        Long id = transaction.getId();
        if (id == null) {
            long newId = ThreadLocalRandom.current().nextLong();
            while (transactions.putIfAbsent(newId, transaction) != null) {
                newId = ThreadLocalRandom.current().nextLong();
            }
            transaction.setId(newId);
        } else {
            synchronized (this) {
                Transaction oldTransaction = transactions.putIfAbsent(id, transaction);
                if (oldTransaction != null) {
                    log.info("{} is replaced by {}.", oldTransaction, transaction);
                    transactions.put(id, transaction);
                }
            }
        }
    }

    /// Возвращает коллекцию всех обработанных транзакций
    public Collection<Transaction> getTransactions() {
        return transactions.values();
    }

    public void clearTransactions() {
        transactions.clear();
    }
}
