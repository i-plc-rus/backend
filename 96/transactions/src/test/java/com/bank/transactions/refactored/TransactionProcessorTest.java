package com.bank.transactions.refactored;

import com.bank.transactions.old.OldTransaction;
import com.bank.transactions.old.OldTransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionProcessorTest {

    @Autowired
    TransactionProcessor processor;

    @Autowired
    OldTransactionProcessor oldProcessor;

    @Test
    @Order(1)
    void warmUp() {
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TestOld {
        List<OldTransaction> oldTransactions = new ArrayList<>();

        @BeforeEach
        void beforeEach() {
            oldTransactions.clear();
        }

        /// Старая программа успешно обрабатывает ожидающие транзакции
        @Test
        @Order(1)
        void PendingStatusOld() {
            oldTransactions.add(new OldTransaction("1", 1841, "2023-01-01", "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа не влияет на совершённые транзакции
        @Test
        @Order(2)
        void CompletedStatusOld() {
            oldTransactions.add(new OldTransaction("1", 1841, "2023-01-01", "COMPLETED"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("COMPLETED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа допускает ошибку в статусе транзакции, которая помешает обработке
        @Test
        @Order(3)
        void MisprintStatusOld() {
            oldTransactions.add(new OldTransaction("1", 1841, "2023-01-01", "PENDIN"));

            oldProcessor.processTransactions(oldTransactions);

            assertNotEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа пишет ошибку в лог для транзакций без статуса, без указания её происхождения
        @Test
        @Order(4)
        void NullStatusOld() {
            oldTransactions.add(new OldTransaction("1", 1841, "2023-01-01", null));

            assertDoesNotThrow(() -> oldProcessor.processTransactions(oldTransactions));
        }

        /// Старая программа при передаче `null` вместо списка транзакций выбрасывает `NullPointerException`
        @Test
        @Order(5)
        void NullListOld() {
            assertThrows(NullPointerException.class, () -> oldProcessor.processTransactions(null));
        }

        /// Пустой список транзакций никак не влияет на исполнение старой программы
        @Test
        @Order(6)
        void EmptyListOld() {
            oldTransactions.clear();

            assertDoesNotThrow(() -> oldProcessor.processTransactions(oldTransactions));
        }

        /// Старая программа выбрасывает исключение, если в списке вместо транзакции оказался `null`-элемент
        @Test
        @Order(7)
        void NullTransactionOld() {
            oldTransactions.add(null);

            assertThrows(NullPointerException.class, () -> oldProcessor.processTransactions(oldTransactions));
        }

        /// Старая программа обрабатывает транзакции с отрицательной суммой
        @Test
        @Order(8)
        void NegativeAmountOld() {
            oldTransactions.add(new OldTransaction("1", -1841, "2023-01-01", "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа обрабатывает транзакции с нулевой суммой
        @Test
        @Order(9)
        void ZeroAmountOld() {
            oldTransactions.add(new OldTransaction("1", 0, "2023-01-01", "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа допускает ошибки округления в сумме транзакции
        @Test
        @Order(10)
        void AmountAccuracyOld() {
            BigDecimal amount = BigDecimal.valueOf(1111111111111111111L);
            oldTransactions.add(new OldTransaction("1", amount.doubleValue(), "2023-01-01", "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
            assertNotEquals(amount, BigDecimal.valueOf(oldTransactions.getFirst().getAmount()));
        }

        /// Старая программа обрабатывает транзакции из будущего
        @Test
        @Order(11)
        void FutureDateOld() {
            oldTransactions.add(new OldTransaction("1", 1841, "2025-01-01", "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа обрабатывает транзакции с неправильной датой
        @Test
        @Order(12)
        void MisprintDateOld() {
            oldTransactions.add(new OldTransaction("1", 1841, "2023-32-32", "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа обрабатывает транзакции без даты
        @Test
        @Order(13)
        void NullDateOld() {
            oldTransactions.add(new OldTransaction("1", 1841, null, "PENDING"));

            oldProcessor.processTransactions(oldTransactions);

            assertEquals("PROCESSED", oldTransactions.getFirst().getStatus());
        }

        /// Старая программа обрабатывает транзакции без `id` и оставляет их `null`
        @Test
        @Order(14)
        void NullIdOld() {
            oldTransactions.add(new OldTransaction(null, 1841, "2023-01-01", "PENDING"));
            oldTransactions.add(new OldTransaction(null, 1841, "2023-01-02", "PENDING"));
            oldTransactions.add(new OldTransaction(null, 1841, "2023-01-03", "PENDING"));

            assertDoesNotThrow(() -> oldProcessor.processTransactions(oldTransactions));

            for (OldTransaction transaction : oldTransactions) {
                assertEquals("PROCESSED", transaction.getStatus());
                assertNull(transaction.getId());
            }
        }

        /// Старая программа обрабатывает транзакции с одинаковым `id` и обрабатывает их как разные транзакции
        @Test
        @Order(15)
        void SameIdOld() {
            List<OldTransaction> transactions = new ArrayList<>();

            transactions.add(new OldTransaction("1L", 1841, "2023-01-01", "PENDING"));
            transactions.add(new OldTransaction("1L", 1841, "2023-01-02", "PENDING"));
            transactions.add(new OldTransaction("1L", 1841, "2023-01-03", "PENDING"));

            oldProcessor.processTransactions(transactions);

            for (OldTransaction transaction : oldTransactions) {
                assertEquals("PROCESSED", transaction.getStatus());
            }
        }

        /// Приблизительный тест на скорость выполнения старой программы (через профайлер)
        @Order(0)
        @RepeatedTest(3)
        void Process2KKOld() {
            Random random = new Random();

            for (long i = 0; i < 2000000; i++) {
                oldTransactions.add(new OldTransaction(Long.toString(i), random.nextInt(1,10002), ZonedDateTime.now().toString(), "PENDING"));
            }

            oldProcessor.processTransactions(oldTransactions);

            for (OldTransaction oldTransaction : oldTransactions) {
                assertEquals("PROCESSED", oldTransaction.getStatus());
            }
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TestRefactored {
        List<Transaction> transactions = new ArrayList<>();
        ZonedDateTime DATE20230101 = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+0"));

        @BeforeEach
        void beforeEach() {
            transactions.clear();
            processor.clearTransactions();
        }

        /// Новая программа успешно обрабатывает ожидающие транзакции
        @Test
        @Order(1)
        void PendingStatusRefactored() {
            transactions.add(new Transaction(1L, 1841, DATE20230101, TransactionStatus.PENDING));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.PROCESSED, transactions.getFirst().getStatus());
        }

        /// Новая программа не обрабатывает завершённые транзакции
        @Test
        @Order(2)
        void CompletedStatusRefactored() {
            transactions.add(new Transaction(1L, 1841, DATE20230101, TransactionStatus.COMPLETED));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.COMPLETED, transactions.getFirst().getStatus());
            assertEquals(0, processor.getTransactions().size());
        }

        /// Новая программа отклоняет транзакции без статуса и пишет об этом в лог
        @Test
        @Order(3)
        void NullStatusRefactored() {
            transactions.add(new Transaction(1L, 1841, DATE20230101, null));

            assertDoesNotThrow(() -> processor.processTransactions(transactions));
            assertEquals(TransactionStatus.CANCELLED, transactions.getFirst().getStatus());
            assertEquals(0, processor.getTransactions().size());
        }

        /// Новая программа при передаче `null` вместо списка транзакций выводит в лог сообщение и продолжает работу
        @Test
        @Order(4)
        void NullListRefactored() {
            assertDoesNotThrow(() -> processor.processTransactions(null));
        }

        /// Пустой список транзакций никак не влияет на исполнение новой программы
        @Test
        @Order(5)
        void EmptyListRefactored() {
            transactions.clear();

            assertDoesNotThrow(() -> processor.processTransactions(transactions));
        }

        /// Новая программа пишет сообщение в лог, если в списке вместо транзакции оказался `null`-элемент
        @Test
        @Order(6)
        void NullTransactionRefactored() {
            transactions.add(null);

            assertDoesNotThrow(() -> processor.processTransactions(transactions));
        }

        /// Новая программа пишет сообщение в лог и отклоняет транзакцию, если сумма отрицательная
        @Test
        @Order(7)
        void NegativeAmountRefactored() {
            transactions.add(new Transaction(1L, -1841, DATE20230101, TransactionStatus.PENDING));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.CANCELLED, transactions.getFirst().getStatus());
            assertEquals(0, processor.getTransactions().size());
        }


        /// Новая программа пишет сообщение в лог и отклоняет транзакцию, если сумма нулевая
        @Test
        @Order(8)
        void ZeroAmountRefactored() {
            transactions.add(new Transaction(1L, 0, DATE20230101, TransactionStatus.PENDING));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.CANCELLED, transactions.getFirst().getStatus());
            assertEquals(0, processor.getTransactions().size());
        }

        /// Новая программа использует целые числа, поэтому ошибок округления нет
        @Test
        @Order(9)
        void AmountAccuracyRefactored() {
            Long amount = 1111111111111111111L;
            transactions.add(new Transaction(1L, amount, DATE20230101, TransactionStatus.PENDING));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.PROCESSED, transactions.getFirst().getStatus());
            assertEquals(amount, processor.getTransactions().iterator().next().getAmount());
        }

        /// Новая программа не пропускает транзакции с неправильной датой и пишет об этом в лог
        @Test
        @Order(10)
        void FutureDateRefactored() {
            transactions.add(new Transaction(1L, 1841, DATE20230101.plusYears(2), TransactionStatus.PENDING));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.CANCELLED, transactions.getFirst().getStatus());
            assertEquals(0, processor.getTransactions().size());
        }

        /// Новая программа не пропускает транзакции без даты и пишет об этом в лог
        @Test
        @Order(11)
        void NullDateRefactored() {
            transactions.add(new Transaction(1L, 1841, null, TransactionStatus.PENDING));

            processor.processTransactions(transactions);

            assertEquals(TransactionStatus.CANCELLED, transactions.getFirst().getStatus());
            assertEquals(0, processor.getTransactions().size());
        }

        /// Новая программа обрабатывает транзакции без `id` и присваивает им новые значения
        @Test
        @Order(12)
        void NullIdRefactored() {
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(null, 1841, DATE20230101, TransactionStatus.PENDING));
            transactions.add(new Transaction(null, 1841, DATE20230101.plusDays(1), TransactionStatus.PENDING));
            transactions.add(new Transaction(null, 1841, DATE20230101.plusDays(2), TransactionStatus.PENDING));

            assertDoesNotThrow(() -> processor.processTransactions(transactions));

            for (Transaction transaction : transactions) {
                assertEquals(TransactionStatus.PROCESSED, transaction.getStatus());
                assertNotNull(transaction.getId());
            }
        }

        /// Новая программа обрабатывает транзакции с одинаковым `id`, обрабатывает их, но записывает только одну из них и
        /// выводит сообщение в лог, когда изменяет запись в хранилище
        @Test
        @Order(13)
        void SameIdRefactored() {
            List<Transaction> transactions = new ArrayList<>();

            transactions.add(new Transaction(1L, 1841, DATE20230101, TransactionStatus.PENDING));
            processor.processTransactions(transactions);

            transactions.add(new Transaction(1L, 1841, DATE20230101.plusDays(1), TransactionStatus.PENDING));
            processor.processTransactions(transactions);

            transactions.add(new Transaction(1L, 1841, DATE20230101.plusDays(2), TransactionStatus.PENDING));
            processor.processTransactions(transactions);

            for (Transaction transaction : transactions) {
                assertEquals(TransactionStatus.PROCESSED, transaction.getStatus());
            }

            assertEquals(1, processor.getTransactions().size());
        }

        /// Приблизительный тест на скорость выполнения новой программы (через профайлер)
        @Order(0)
        @RepeatedTest(3)
        void Process2KKRefactored() {
            Random random = new Random();

            for (long i = 0; i < 2000000; i++) {
                transactions.add(new Transaction(null, random.nextInt(1,10002), ZonedDateTime.now(), TransactionStatus.PENDING));
            }

            processor.processTransactions(transactions);

            for (Transaction transaction : transactions) {
                assertEquals(TransactionStatus.PROCESSED, transaction.getStatus());
            }
        }
    }
}
