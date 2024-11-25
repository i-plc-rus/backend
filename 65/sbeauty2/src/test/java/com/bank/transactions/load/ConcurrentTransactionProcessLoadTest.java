package com.bank.transactions.load;

import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionBatchProcessor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static com.bank.transactions.data.model.TransactionStatus.PENDING;
import static com.bank.transactions.utils.RandomObjectGenerator.getRandomString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(properties = { "transaction.repository.type=PARALLEL", "transaction.processor.type=PARALLEL" })
class ConcurrentTransactionProcessLoadTest {

    @Autowired
    private TransactionBatchProcessor transactionBatchProcessor;
    @Autowired
    private ForkJoinPool forkJoinPool;

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1_000, 10_000, 100_000, 300_000 })
    void testProcessTransactions(int transactionCount) throws InterruptedException {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < transactionCount; i++) {
            transactions.add(new Transaction(getRandomString(), BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), PENDING));
        }
        LocalDateTime start = LocalDateTime.now();
        assertDoesNotThrow(() -> transactionBatchProcessor.processTransactions(transactions));

        long startTime = System.currentTimeMillis();
        long timeoutInMillis = 20_000;
        while (forkJoinPool.getActiveThreadCount() != 0 && System.currentTimeMillis() - startTime < timeoutInMillis) {
            Thread.sleep(5);
        }
        System.out.printf(
                "%d transactions were processed during %d ms%n",
                transactionCount,
                Duration.between(start, LocalDateTime.now()).toMillis()
        );
    }
}
