package com.bank.transactions.service;

import com.bank.transactions.util.TransactionFactory;
import com.bank.transactions.model.Transaction;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

@Component
public final class TransactionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);

    private final TransactionService transactionService;
    private final TransactionFactory transactionFactory;

    @Autowired
    public TransactionProcessor(@NonNull final TransactionService transactionService,
                                @NonNull final TransactionFactory transactionFactory) {
        this.transactionService = transactionService;
        this.transactionFactory = transactionFactory;
    }

    /**
     * Метод для обработки списка транзакций.
     *
     * @param transactions список транзакций для обработки
     * <p>
     * Этот метод использует параллельный поток (parallelStream) для оптимизации производительности.
     * Он фильтрует null-значения, подготавливает каждую транзакцию и затем обрабатывает их.
     * Использование параллельного потока позволяет обрабатывать несколько транзакций одновременно,
     * что особенно эффективно на многоядерных процессорах.
     * </p>
     */
    public void processTransactions(@Nullable final List<Transaction> transactions) {
        if (transactions == null) {
            logger.warn("Received null transaction list. No transactions to process.");
            return;
        }

        transactions.parallelStream()
                .filter(Objects::nonNull)
                .map(this::prepareTransaction)
                .forEach(this::processTransaction);
    }

    /**
     * Метод для подготовки транзакции к обработке.
     *
     * @param transaction исходная транзакция
     * @return подготовленная транзакция
     * <p>
     * Этот метод создает новую транзакцию на основе существующей, используя TransactionFactory.
     * Это позволяет стандартизировать формат транзакций перед их обработкой.
     * Аннотация @NonNull гарантирует, что метод всегда возвращает непустой объект.
     * </p>
     */
    private @NonNull Transaction prepareTransaction(@NonNull final Transaction transaction) {
        return transactionFactory.createTransaction(transaction.getAmount(), transaction.getDate());
    }

    /**
     * Метод для обработки отдельной транзакции.
     *
     * @param transaction транзакция для обработки
     * <p>
     * Этот метод обрабатывает одну транзакцию, логирует крупные транзакции и обрабатывает исключения.
     * Выделение этой логики в отдельный метод улучшает читаемость кода и облегчает его поддержку.
     * Обработка исключений здесь позволяет продолжить обработку других транзакций в случае ошибки.
     * </p>
     */
    private void processTransaction(@NonNull final Transaction transaction) {
        try {
            if (transaction.getAmount() > 10000) {
                logger.info("Processing large transaction: {}", transaction.getId());
            }
            transactionService.processTransaction(transaction);
        } catch (final Exception e) {
            logger.error("Error processing transaction {}: {}", transaction.getId(), e.getMessage());
        }
    }
}