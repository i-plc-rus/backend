package com.example.proj;

import com.example.proj.exceptions.TransactionAlreadyProcessedException;
import com.example.proj.exceptions.TransactionProcessingException;
import com.example.proj.logger.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TransactionProcessor {

    private final TransactionService transactionService;
    private final AppLogger logger;

    private static final int BATCH_SIZE = 1000;

    @Autowired
    public TransactionProcessor(TransactionService transactionService, AppLogger logger) {
        this.transactionService = transactionService;
        this.logger = logger;
    }

    @Async
    public CompletableFuture<Void> processTransactions(List<Transaction> transactions) {
        int numberOfBatches = (int) Math.ceil((double) transactions.size() / BATCH_SIZE);

        // Обрабатываем батчи транзакций параллельно
        List<CompletableFuture<Void>> futures = IntStream.range(0, numberOfBatches)
                .mapToObj(batchIndex -> {
                    int fromIndex = batchIndex * BATCH_SIZE;
                    int toIndex = Math.min(fromIndex + BATCH_SIZE, transactions.size());
                    List<Transaction> batch = transactions.subList(fromIndex, toIndex);
                    return processBatchAsync(batch);
                })
                .collect(Collectors.toList());

        // Ожидание завершения всех асинхронных задач
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }


    private CompletableFuture<Void> processBatchAsync(List<Transaction> batch) {
        return CompletableFuture.runAsync(() -> processBatch(batch));
    }

    private void processBatch(List<Transaction> batch) {
        batch.forEach(transaction -> {
            try {
                transactionService.processTransaction(transaction);
            } catch (TransactionAlreadyProcessedException e) {
                logger.log("Transaction already processed: " + e.getMessage());
            } catch (TransactionProcessingException e) {
                logger.log("Error processing transaction: " + e.getMessage());
            } catch (Exception e) {
                logger.log("Unexpected error: " + e.getMessage());
            }
        });
    }
}
