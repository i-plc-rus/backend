package com.bank.transactions.v2;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.bank.transactions.Transaction;
import com.bank.transactions.TransactionProcessor;
import com.bank.transactions.v2.TransactionStatus;
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
        int transactionCount = 1000;

        var durationV1 = measureDuration(transactionProcessorV1::processTransactions, generateTransactions(transactionCount, "V1"));
        var durationV2 = measureDuration(transactionProcessorV2::processTransactions, generateTransactions(transactionCount, "V2"));

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

    private List<Transaction> generateTransactions(int count, String version) {
        List<Transaction> transactions = new ArrayList<>(count);
        while (transactions.size() < count) {
            transactions.add(generateTransaction(version));
        }
        return transactions;
    }

    private Transaction generateTransaction(String version) {
        String id = diceString(predefinedIds);
        double amount = diceDouble();
        String date = diceString(predefinedDates);
        String status = diceString(predefinedStatuses);
        if ("V1".equals(version)) {
            return new com.bank.transactions.v1.Transaction(id, amount, date, status);
        } else if ("V2".equals(version)) {
            return new com.bank.transactions.v2.Transaction(id, amount, date, status);
        } else {
            throw new IllegalArgumentException("version: " + version);
        }
    }

    // Возвращает случайным образом одну из предопределенных строк, случайную строку либо null
    private String diceString(List<String> predefined) {
        var side = random.nextInt(predefined.size() + 2);
        if (side < predefined.size()) {
            return predefined.get(side);
        } else if (side == predefined.size()) {
            return UUID.randomUUID().toString().substring(0, 7);
        } else {
            return null;
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
