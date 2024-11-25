package com.bank.transactions.service;

import com.bank.transactions.dao.TransactionRepository;
import com.bank.transactions.dto.Transaction;
import com.bank.transactions.util.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class TransactionProcessor {
    private final TransactionRepository repository;
    private final Logger logger;
    private final ExecutorService executor;

    public TransactionProcessor(TransactionRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
        this.executor = Executors.newWorkStealingPool();
    }

    /**
     * Метод обработки транзакций в многопоточном режиме.
     * Проверяет статус транзакций и для транзакций со статусом PENDING,
     * запускает обработку в отдельном потоке
     *
     * @param transactions список транзакций
     */
    public void processTransactions(List<Transaction> transactions) {
        if (transactions != null && !transactions.isEmpty()) {
            CountDownLatch latch = new CountDownLatch(transactions.size());
            transactions.forEach(transaction -> {
                if (transaction.getStatus().equals("PENDING")) {
                    executor.execute(() -> processTransaction(transaction, latch));
                } else {
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.error("Error processing transactions.", e);
            }
        }
    }

    private void processTransaction(Transaction transaction, CountDownLatch latch) {
        try {
            if (transaction.getAmount() > 10000) {
                logger.log("Processing large transaction: " + transaction.getId());
            }
            transaction.setStatus("PROCESSED");
            repository.updateTransaction(transaction);
        } catch (Exception e) {
            logger.error("Error processing transaction: " + transaction.getId(), e);
        } finally {
            latch.countDown();
        }
    }
}
