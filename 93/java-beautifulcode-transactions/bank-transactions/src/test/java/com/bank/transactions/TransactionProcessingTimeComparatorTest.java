package com.bank.transactions;

import com.bank.transactions.model.Transaction;
import com.bank.transactions.service.TransactionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class TransactionProcessingTimeComparatorTest {
    private static final Random random = new Random();

    @Autowired
    @Qualifier("ListBasedTransactionProcessor")
    private TransactionProcessor listBasedProcessor;

    @Autowired
    @Qualifier("MapBasedTransactionProcessor")
    private TransactionProcessor mapBasedProcessor;

    // Чем большее кол-во транзакций обрабатывается, тем нагляднее выражена разница в длительности обработки
    @Test
    void whenExecuteLargeNumberOfTransactions_executingTimeOfListMapBasedRepoGreaterThenMapBased() throws ExecutionException, InterruptedException {
        int transactionsCount = 50_000;

        long timeListMapBasedProcessor = processTransactions(listBasedProcessor, transactionsCount);
        long timeForMapBasedProcessor = processTransactions(mapBasedProcessor, transactionsCount);

        assertTrue(timeListMapBasedProcessor > timeForMapBasedProcessor);

        log.info("Time for list based repository: {} ms", timeListMapBasedProcessor);
        log.info("Time for map based repository: {} ms", timeForMapBasedProcessor);
    }

    long processTransactions(TransactionProcessor processor, int count) throws ExecutionException, InterruptedException {
        long from = System.currentTimeMillis();

        List<Long> createdTds = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CompletableFuture<Transaction> transaction =
                    processor.addTransaction(random.nextDouble(-9999, 9999));
            createdTds.add(transaction.get().getId());
        }
        processor.processTransactions(createdTds);

        long to = System.currentTimeMillis();
        return to - from;
    }
}
