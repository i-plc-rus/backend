package com.bank.transactions.processor;

import com.bank.transactions.BaseTest;
import com.bank.transactions.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class MultithreadingTest extends BaseTest {

    /**
     * Start 10 threads each trying to process 100k transactions.
     * All transactions are properly saved expected.
     */
    @Test
    public void testProcessingFromMultipleTreads() throws InterruptedException {
        List<Transaction> list = createPendingTransactions(1_000_000);
        List<Runnable> runnables = List.of(
                () -> newProcessor.processTransactions(list.subList(0, 100_000)),
                () -> newProcessor.processTransactions(list.subList(100_000, 200_000)),
                () -> newProcessor.processTransactions(list.subList(200_000, 300_000)),
                () -> newProcessor.processTransactions(list.subList(300_000, 400_000)),
                () -> newProcessor.processTransactions(list.subList(400_000, 500_000)),
                () -> newProcessor.processTransactions(list.subList(500_000, 600_000)),
                () -> newProcessor.processTransactions(list.subList(600_000, 700_000)),
                () -> newProcessor.processTransactions(list.subList(700_000, 800_000)),
                () -> newProcessor.processTransactions(list.subList(800_000, 900_000)),
                () -> newProcessor.processTransactions(list.subList(900_000, 1_000_000))
        );

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        runnables.forEach(executorService::execute);
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(2, TimeUnit.SECONDS);

        List<Transaction> transactions = repository.getTransactions();
        assertThat(terminated).isTrue();
        assertThat(transactions.size()).isEqualTo(1_000_000);
    }
}