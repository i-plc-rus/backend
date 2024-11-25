package com.bank.transactions.app.services;

import com.bank.transactions.app.models.Transaction;
import com.bank.transactions.app.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс обработки транзакций, включающий валидацию, обновление состояния и логирование
 */
@Component
public class TransactionProcessor {

    private final TransactionRepository repository;
    private final Logger logger;

    private static final String PENDING_STATUS = "PENDING";
    private static final String PROCESSED_STATUS = "PROCESSED";
    private static final double LARGE_TRANSACTION_THRESHOLD = 10000.0;

    /**
     * Конструктор с определением репозитория и логгера
     *
     * @param repository репозиторий для сохранения транзакций
     * @param logger     логгер для хранения сообщений
     */
    @Autowired
    public TransactionProcessor(TransactionRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    /**
     * Обработка списка транзакций
     * Только транзакции со статусом "PENDING" будут обработаны
     *
     * @param transactions список транзакция для обработки
     */
    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (PENDING_STATUS.equals(transaction.getStatus())) {
                processTransaction(transaction);
            }
        }
    }

    /**
     * Обработка единичной транзакции
     * Этот метод проверяет транзакцию, обновляет статус, логгирует изменения
     * и сохраняет транзакцию в репозитории
     *
     * @param transaction транзакция для обработки
     */
    private void processTransaction(Transaction transaction) {
        try {
            validateTransaction(transaction);
            logTransactionIfLarge(transaction);
            String oldStatus = transaction.getStatus();
            transaction.setStatus(PROCESSED_STATUS);
            logger.log(String.format("Transaction %s status changed from %s to %s",
                    transaction.getId(), oldStatus, transaction.getStatus()));
            repository.saveTransaction(transaction);
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
        }
    }

    /**
     * Проверка определенной транзакции
     * Этот метод проверяет размер транзакции на положительность
     * и на уникальность в репозитории
     *
     * @param transaction транзакция для проверки
     * @throws IllegalArgumentException если размер транзакции отрицателен
     *                                  или если ID транзакции не уникален
     */
    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }
        if (repository.getTransactions().stream().anyMatch(t -> t.getId().equals(transaction.getId()))) {
            throw new IllegalArgumentException("Transaction ID must be unique");
        }
    }

    /**
     * Логгирование сообщения если размер транзакции больше определенного в LARGE_TRANSACTION_THRESHOLD
     *
     * @param transaction транзакция для проверки
     */
    private void logTransactionIfLarge(Transaction transaction) {
        if (transaction.getAmount() > LARGE_TRANSACTION_THRESHOLD) {
            logger.log("Processing large transaction: " + transaction.getId());
        }
    }
}
