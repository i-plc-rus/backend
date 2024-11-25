package org.example.catalogue.management.beautiful.code.afal;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionProcessor {

    private final TransactionRepository repository;
    private final ExecutorService smallTransactionExecutorService;
    private final ExecutorService largeTransactionExecutorService;
    private final BlockingQueue<Transaction> smallTransactionQueue;
    private final BlockingQueue<Transaction> largeTransactionQueue;

    @Value("${app.max-retry-count:3}")
    private int MAX_RETRY_COUNT;

    @PostConstruct
    public void init() {
        for (int i = 0; i < ((ThreadPoolExecutor) smallTransactionExecutorService).getCorePoolSize(); i++) {
            smallTransactionExecutorService.submit(this::processSmallTransactionsFromQueue);
        }

        for (int i = 0; i < ((ThreadPoolExecutor) largeTransactionExecutorService).getCorePoolSize(); i++) {
            largeTransactionExecutorService.submit(this::processLargeTransactionsFromQueue);
        }
    }

    public void submitTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            log.debug("Список транзакций пуст - пропускаем обработку");
            return;
        }

        transactions.stream().filter(Transaction::isPending).forEach(transaction -> {
            try {
                if (transaction.isLarge()) {
                    largeTransactionQueue.put(transaction);
                } else {
                    smallTransactionQueue.put(transaction);
                }
            } catch (InterruptedException e) {
                log.error("Ошибка при добавлении транзакции в очередь", e);
                Thread.currentThread().interrupt();
            }
        });

        log.info("Добавлено {} транзакций в очереди для обработки", transactions.size());
    }

    private void processSmallTransactionsFromQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Transaction transaction = smallTransactionQueue.take();
                processTransactionWithRetries(transaction);
            } catch (InterruptedException e) {
                log.warn("Поток обработки мелких транзакций прерван", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processLargeTransactionsFromQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Transaction transaction = largeTransactionQueue.take();
                processTransactionWithRetries(transaction);
            } catch (InterruptedException e) {
                log.warn("Поток обработки крупных транзакций прерван", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processTransactionWithRetries(Transaction transaction) {
        int attempt = 0;
        while (attempt < MAX_RETRY_COUNT) {
            try {
                processTransaction(transaction);
                return;
            } catch (Exception e) {
                attempt++;
                log.warn("Попытка {}/{} обработки транзакции {} завершилась ошибкой", attempt, MAX_RETRY_COUNT,
                    transaction.id(), e
                );
            }
        }
        log.error("Не удалось обработать транзакцию {} после {} попыток", transaction.id(), MAX_RETRY_COUNT);
    }

    private void processTransaction(Transaction transaction) {
        repository.updateTransaction(transaction.makeProcessed());
    }

    @PreDestroy
    public void shutdown() {
        smallTransactionExecutorService.shutdownNow();
        largeTransactionExecutorService.shutdownNow();
        log.info("Завершение работы пулов потоков обработки транзакций");
    }
}
