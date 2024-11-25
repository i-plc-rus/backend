package com.example.proj;

import com.example.proj.logger.AppLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor transactionProcessor;

    private List<Transaction> transactions;

    @BeforeEach
    public void setup() {
        // Инициализируем новый список транзакций перед каждым тестом
        transactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactions.add(new Transaction("T" + i, 10000 + i, "2024-01-01", TransactionStatus.PENDING));
        }
    }

    // 1. Успешная обработка всех транзакций
    @Test
    public void testProcessTransactionsSuccess() {
        // Запускаем асинхронную обработку и дожидаемся её завершения
        CompletableFuture<Void> future = transactionProcessor.processTransactions(transactions);
        future.join();  // Ожидаем завершения всех асинхронных задач

        // Проверяем, что все транзакции были обработаны
        for (Transaction transaction : transactions) {
            assertEquals(TransactionStatus.PROCESSED, transaction.getStatus());
        }
    }

    // 2. Обработка транзакции, которая уже была обработана (TransactionAlreadyProcessedException)
    @Test
    public void testTransactionAlreadyProcessedException() {
        // Устанавливаем статус одной транзакции как PROCESSED
        transactions.get(0).setStatus(TransactionStatus.PROCESSED);

        CompletableFuture<Void> future = transactionProcessor.processTransactions(transactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Первая транзакция должна остаться в статусе PROCESSED, остальные должны быть обработаны
        assertEquals(TransactionStatus.PROCESSED, transactions.get(0).getStatus());

        for (int i = 1; i < transactions.size(); i++) {
            assertEquals(TransactionStatus.PROCESSED, transactions.get(i).getStatus());
        }
    }

    // 3. Проверка обработки крупной транзакции (сумма > 100,000)
    @Test
    public void testLargeTransaction() {
        // Устанавливаем сумму для одной из транзакций > 100,000
        transactions.get(5).setAmount(150000);

        CompletableFuture<Void> future = transactionProcessor.processTransactions(transactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Проверяем, что все транзакции были успешно обработаны
        transactions.forEach(transaction -> assertEquals(TransactionStatus.PROCESSED, transaction.getStatus()));
    }

    // 4. Пограничный случай: Пустой список транзакций
    @Test
    public void testProcessEmptyTransactionsList() {
        List<Transaction> emptyTransactions = new ArrayList<>();

        CompletableFuture<Void> future = transactionProcessor.processTransactions(emptyTransactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Проверяем, что пустой список обработался без ошибок
        assertTrue(emptyTransactions.isEmpty());
    }

    // 5. Проверка, что транзакция с отсутствующим статусом выбрасывает исключение
    @Test
    public void testTransactionNotFoundException() {
        transactions.get(3).setStatus(null);  // Некорректная транзакция, статус отсутствует

        CompletableFuture<Void> future = transactionProcessor.processTransactions(transactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Проверяем, что остальные транзакции были успешно обработаны
        for (int i = 0; i < transactions.size(); i++) {
            if (i != 3) {
                assertEquals(TransactionStatus.PROCESSED, transactions.get(i).getStatus());
            } else {
                // Ожидаем, что некорректная транзакция осталась без обработки
                assertNull(transactions.get(3).getStatus());
            }
        }
    }

    // 6. Обработка исключения TransactionProcessingException для транзакции с отрицательной суммой
    @Test
    public void testTransactionProcessingException() {
        transactions.get(2).setAmount(-1000);  // Некорректная транзакция с отрицательной суммой
        // Запускаем асинхронную обработку и дожидаемся её завершения
        CompletableFuture<Void> future = transactionProcessor.processTransactions(transactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Проверяем, что транзакции обработаны, кроме некорректной
        for (int i = 0; i < transactions.size(); i++) {
            if (i != 2) {
                assertEquals(TransactionStatus.PROCESSED, transactions.get(i).getStatus());
            } else {
                // Некорректная транзакция с ошибкой
                assertEquals(TransactionStatus.PENDING, transactions.get(2).getStatus());
            }
        }
    }

    // 7. Тест на производительность для большого числа транзакций
    @Test
    public void testPerformanceWithLargeTransactionsList() {
        // Создаем 10,000 транзакций для проверки производительности
        List<Transaction> largeTransactions = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            largeTransactions.add(new Transaction("T" + i, 1000 + i, "2024-01-01", TransactionStatus.PENDING));
        }

        // Засекаем время начала обработки
        long startTime = System.nanoTime();

        CompletableFuture<Void> future = transactionProcessor.processTransactions(largeTransactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Засекаем время окончания обработки
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;  // Конвертируем в миллисекунды

        System.out.println("Time taken to process 10,000 transactions: " + duration + " ms");

        // Проверяем, что все транзакции были успешно обработаны
        largeTransactions.forEach(transaction -> assertEquals(TransactionStatus.PROCESSED, transaction.getStatus()));

        // Проверяем, что обработка заняла разумное время (< 5 секунд)
        assertTrue(duration < 5000, "Processing took too long!");
    }

    // 8. Тест на производительность для очень большого числа транзакций
    @Test
    public void testPerformanceWithHugeTransactionsList() {
        // Создаем 100,000 транзакций для проверки производительности
        List<Transaction> hugeTransactions = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            hugeTransactions.add(new Transaction("T" + i, 1000 + i, "2024-01-01", TransactionStatus.PENDING));
        }

        // Засекаем время начала обработки
        long startTime = System.nanoTime();

        CompletableFuture<Void> future = transactionProcessor.processTransactions(hugeTransactions);
        future.join();  // Ожидание завершения асинхронной обработки

        // Засекаем время окончания обработки
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;  // Конвертируем в миллисекунды

        System.out.println("Time taken to process 100,000 transactions: " + duration + " ms");

        // Проверяем, что все транзакции были успешно обработаны
        hugeTransactions.forEach(transaction -> assertEquals(TransactionStatus.PROCESSED, transaction.getStatus()));

        // Проверяем, что обработка заняла разумное время (< 30 секунд)
        assertTrue(duration < 30000, "Processing took too long!");
    }


}
