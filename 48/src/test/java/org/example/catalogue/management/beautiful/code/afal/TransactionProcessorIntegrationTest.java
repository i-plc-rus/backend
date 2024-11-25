package org.example.catalogue.management.beautiful.code.afal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionProcessorIntegrationTest {

    @Autowired
    private TransactionProcessor processor;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    public void test_processTransactionsWithExceptionInOneTransaction() {
        Transaction transaction1 =
            new Transaction("1", BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), TransactionStatus.PENDING);
        Transaction transaction2 =
            new Transaction(null, BigDecimal.valueOf(15000), LocalDate.parse("2023-01-02"), TransactionStatus.PENDING);
        Transaction transaction3 =
            new Transaction("3", BigDecimal.valueOf(2000), LocalDate.parse("2023-01-03"), TransactionStatus.PENDING);

        processor.processTransactions(List.of(transaction1, transaction2, transaction3));

        List<Transaction> updatedTransactions = transactionRepository.getTransactions();

        Assertions.assertEquals(2,
            updatedTransactions.stream().filter(t -> t.status() == TransactionStatus.PROCESSED).count(),
            "Две транзакции должны быть обработаны"
        );

        Assertions.assertTrue(
            updatedTransactions.stream().anyMatch(t -> t.id().equals("1") && t.status() == TransactionStatus.PROCESSED),
            "Транзакция 1 должна быть обработана"
        );
        Assertions.assertTrue(
            updatedTransactions.stream().anyMatch(t -> t.id().equals("3") && t.status() == TransactionStatus.PROCESSED),
            "Транзакция 3 должна быть обработана"
        );

        Assertions.assertFalse(
            updatedTransactions.stream()
                .anyMatch(t -> t.id() == null || t.id().equals("2") && t.status() == TransactionStatus.PENDING),
            "Транзакция 2 не должна быть обработана из-за исключения"
        );
    }

    @Test
    public void test_processAllPendingTransactionsSuccessfully() {
        Transaction transaction1 =
            new Transaction("1", BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), TransactionStatus.PENDING);
        Transaction transaction2 =
            new Transaction("2", BigDecimal.valueOf(15000), LocalDate.parse("2023-01-02"), TransactionStatus.PENDING);
        Transaction transaction3 =
            new Transaction("3", BigDecimal.valueOf(2000), LocalDate.parse("2023-01-03"), TransactionStatus.PENDING);

        processor.processTransactions(List.of(transaction1, transaction2, transaction3));

        List<Transaction> updatedTransactions = transactionRepository.getTransactions();

        Assertions.assertEquals(3, updatedTransactions.size(), "Все три транзакции должны быть обработаны");
        Assertions.assertTrue(
            updatedTransactions.stream().allMatch(t -> t.status() == TransactionStatus.PROCESSED),
            "Все транзакции должны иметь статус PROCESSED"
        );
    }

    @Test
    public void test_processNoPendingTransactions() {
        Transaction transaction1 =
            new Transaction("1", BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), TransactionStatus.COMPLETED);
        Transaction transaction2 =
            new Transaction("2", BigDecimal.valueOf(15000), LocalDate.parse("2023-01-02"), TransactionStatus.PROCESSED);

        processor.processTransactions(List.of(transaction1, transaction2));

        List<Transaction> transactionsAfterProcessing = transactionRepository.getTransactions();

        Assertions.assertEquals(0, transactionsAfterProcessing.size(), "Не должно быть обработанных транзакций");
    }

    @Test
    public void test_lotsOfLargeAndSmallTransactions() {
        List<Transaction> largeTransactions = IntStream.range(0, 1000)
            .mapToObj(
                i -> new Transaction("large" + i, BigDecimal.valueOf(20000L + i * 1000L), LocalDate.parse("2023-01-01"),
                    TransactionStatus.PENDING
                ))
            .collect(Collectors.toList());

        List<Transaction> smallTransactions = IntStream.range(0, 500)
            .mapToObj(
                i -> new Transaction("small" + i, BigDecimal.valueOf(1000L + i * 100L), LocalDate.parse("2023-01-01"),
                    TransactionStatus.PENDING
                ))
            .toList();

        largeTransactions.addAll(smallTransactions);
        processor.processTransactions(largeTransactions);

        List<Transaction> updatedTransactions = transactionRepository.getTransactions();

        Assertions.assertEquals(1500, updatedTransactions.size(), "Общее количество транзакций должно быть 1500");
        Assertions.assertTrue(
            updatedTransactions.stream().allMatch(t -> t.status() == TransactionStatus.PROCESSED),
            "Все транзакции должны иметь статус PROCESSED"
        );

        long processedLargeTransactionsCount = updatedTransactions.stream()
            .filter(t -> t.id().startsWith("large") && t.status() == TransactionStatus.PROCESSED)
            .count();
        Assertions.assertEquals(
            1000, processedLargeTransactionsCount, "Все 1000 тяжелых транзакций должны быть обработаны");

        long processedSmallTransactionsCount = updatedTransactions.stream()
            .filter(t -> t.id().startsWith("small") && t.status() == TransactionStatus.PROCESSED)
            .count();
        Assertions.assertEquals(
            500, processedSmallTransactionsCount, "Все 500 легких транзакций должны быть обработаны");
    }
}
