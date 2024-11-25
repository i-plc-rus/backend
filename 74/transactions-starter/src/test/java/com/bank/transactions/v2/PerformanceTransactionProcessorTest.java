package com.bank.transactions.v2;

import static org.assertj.core.api.Assertions.assertThat;

import com.bank.transactions.Transaction;
import com.bank.transactions.TransactionProcessor;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest
@ContextConfiguration(classes = {
    com.bank.transactions.v1.AutoConfiguration.class,
    com.bank.transactions.v2.AutoConfiguration.class
})
public class PerformanceTransactionProcessorTest {

    private static final List<String> predefinedIds = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
    private static final List<String> predefinedDates = List.of("01.01.2024", "01.02.2024", "01.03.2024", "01.04.2024");
    private static final List<String> predefinedStatuses = List.of(TransactionStatus.PENDING);
    private static final Random random = new Random();

    @Autowired
    @Qualifier("transactionProcessorV1")
    private TransactionProcessor transactionProcessorV1;
    @Autowired
    @Qualifier("transactionProcessorV2")
    private TransactionProcessor transactionProcessorV2;
    @Autowired
    private com.bank.transactions.v1.TransactionRepository transactionRepositoryV1;
    @Autowired
    private com.bank.transactions.v2.TransactionRepository transactionRepositoryV2;
    @Autowired
    private com.bank.transactions.v1.Logger loggerV1;
    @Autowired
    private com.bank.transactions.v2.Logger loggerV2;

    @BeforeEach
    void setUp() {
        transactionRepositoryV1.getTransactions().clear();
        transactionRepositoryV2.deleteAll();
    }

    @Test
    void testPerformance() {
        int transactionCount = 10000;
        var transactions = generateTransactions(transactionCount);
        var transactionsCopy = copy(transactions);

        var durationV1 = measureDuration(transactionProcessorV1::processTransactions, transactions);
        var durationV2 = measureDuration(transactionProcessorV2::processTransactions, transactionsCopy);

        var v1RepoIds = transactionRepositoryV1.getTransactions().stream()
            .map(Transaction::getId)
            .distinct()
            .toList();
        var v2RepoIds = transactionRepositoryV2.getTransactions().stream()
            .map(Transaction::getId)
            .distinct()
            .toList();
        assertThat(v1RepoIds).containsExactlyInAnyOrderElementsOf(v2RepoIds);

        log("--------------------------------");
        log("--- Test Performance Results ---");
        log("--------------------------------");
        log("Processing transactions count: " + transactionCount);
        log("ForkJoin common pool parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        log("V1: duration=" + durationV1.toMillis() + "ms; repositorySize=" + transactionRepositoryV1.getTransactions().size());
        log("V2: duration=" + durationV2.toMillis() + "ms; repositorySize=" + transactionRepositoryV2.getTransactions().size());

        var durationRatio = 1.0 * durationV1.toMillis() / durationV2.toMillis();
        log("V1/V2 durationRatio=" + durationRatio);
        var repositorySizeRatio = 1.0 * transactionRepositoryV1.getTransactions().size() / transactionRepositoryV2.getTransactions().size();
        log("V1/V2 repositorySizeRatio=" + repositorySizeRatio);

        assertThat(durationRatio).isGreaterThan(3);
        assertThat(repositorySizeRatio).isGreaterThan(3);
    }

    private void log(String message) {
        loggerV2.log(this, message);
    }

    private List<Transaction> generateTransactions(int count) {
        List<Transaction> transactions = new ArrayList<>(count);
        while (transactions.size() < count) {
            transactions.add(generateTransaction());
        }
        return transactions;
    }

    private Transaction generateTransaction() {
        String id = diceString(predefinedIds);
        double amount = diceDouble();
        String date = diceString(predefinedDates);
        String status = diceString(predefinedStatuses);
        return transaction(id, amount, date, status);
    }

    private Transaction transaction(String id, double amount, String date, String status) {
        return new com.bank.transactions.v1.Transaction(id, amount, date, status);
    }

    private List<Transaction> copy(List<Transaction> transactions) {
        return transactions.stream()
            .map(e -> transaction(e.getId(), e.getAmount(), e.getDate(), e.getStatus()))
            .toList();
    }

    // Возвращает случайным образом одну из предопределенных строк, либо случайную строку
    private String diceString(List<String> predefined) {
        var side = random.nextInt(predefined.size() + 2);
        if (side < predefined.size()) {
            return predefined.get(side);
        } else {
            return UUID.randomUUID().toString().substring(0, 7);
        }
    }

    private double diceDouble() {
        return random.nextDouble(50000);
    }

    private <T> Duration measureDuration(Consumer<T> consumer, T data) {
        Instant startTime = Instant.now();
        consumer.accept(data);
        return Duration.between(startTime, Instant.now());
    }
}
