package com.bank.transactions.service;

import com.bank.transactions.util.TransactionBatchManager;
import com.bank.transactions.exception.TransactionProcessingException;
import com.bank.transactions.model.Transaction;
import com.bank.transactions.model.TransactionStatus;
import com.bank.transactions.repo.TransactionRepository;
import com.bank.transactions.repo.TransactionSession;
import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository repository;
    private final TransactionBatchManager batchManager;

    @Autowired
    public TransactionServiceImpl(@NonNull final TransactionRepository repository,
                                  @NonNull final TransactionBatchManager batchManager) {
        this.repository = repository;
        this.batchManager = batchManager;
    }

    /**
     * Обрабатывает транзакцию.
     *
     * @param transaction транзакция для обработки
     * <p>
     * Метод использует Optional для безопасной обработки null-значений.
     * Только ожидающие обработки транзакции (PENDING) будут обработаны.
     * </p>
     */
    @Override
    public void processTransaction(@Nullable final Transaction transaction) {
        Optional.ofNullable(transaction)
                .filter(this::isPendingTransaction)
                .ifPresent(this::processTransactionInternal);
    }

    /**
     * Внутренний метод для обработки транзакции.
     *
     * @param transaction транзакция для обработки
     * <p>
     * Метод изменяет статус транзакции, добавляет ее в пакет и,
     * если пакет заполнен, вызывает метод flushBatch для сохранения транзакций.
     * В случае ошибки, статус транзакции меняется на FAILED.
     * </p>
     */
    private void processTransactionInternal(@NonNull final Transaction transaction) {
        try {
            transaction.withStatus(TransactionStatus.PROCESSED);
            batchManager.addToBatch(transaction);
            if (batchManager.isBatchFull()) {
                flushBatch();
            }
        } catch (TransactionProcessingException e) {
            logger.error("Error processing transaction: {}", e.getMessage());
            transaction.withStatus(TransactionStatus.FAILED);
        }
    }

    /**
     * Проверяет, является ли транзакция ожидающей обработки.
     *
     * @param transaction транзакция для проверки
     * @return true, если транзакция ожидает обработки, иначе false
     */
    private boolean isPendingTransaction(@NonNull final Transaction transaction) {
        return transaction.getStatus() == TransactionStatus.PENDING;
    }

    /**
     * Сохраняет пакет транзакций в базу данных.
     * <p>
     * Метод получает текущий пакет транзакций из batchManager и
     * вызывает метод updateTransactions для их сохранения.
     * </p>
     */
    private void flushBatch() {
        List<Transaction> batchToFlush = batchManager.flushBatch();
        updateTransactions(batchToFlush);
    }

    /**
     * Обновляет транзакции в базе данных.
     *
     * @param batchToFlush список транзакций для обновления
     * <p>
     * Метод использует сессию транзакций для обновления данных в базе.
     * В случае ошибки, логирует исключение и выбрасывает TransactionProcessingException.
     * </p>
     */
    private void updateTransactions(@NonNull final List<Transaction> batchToFlush) {
        try (TransactionSession session = repository.openSession()) {
            session.updateTransactions(batchToFlush);
        } catch (Exception e) {
            logger.error("Error updating transactions: {}", e.getMessage());
            throw new TransactionProcessingException("Failed to update transactions", e);
        }
    }

    /**
     * Метод, вызываемый перед уничтожением бина.
     * <p>
     * Обеспечивает сохранение всех оставшихся в пакете транзакций
     * перед завершением работы приложения.
     * </p>
     */
    @PreDestroy
    public void flushRemainingTransactions() {
        if (!batchManager.isBatchEmpty()) {
            flushBatch();
        }
    }
}