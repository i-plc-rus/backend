package ru.competition.transactions.repository;

import org.junit.jupiter.api.Test;
import ru.competition.transactions.repository.impl.ConcurrentTransactionRepositoryImpl;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.competition.transactions.utils.TestConstants.LONG_ONE;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransaction;

public class ConcurrentTransactionRepositoryImplTest {

    private final TransactionRepository repository = new ConcurrentTransactionRepositoryImpl();

    @Test
    void shouldNotUpdateTransaction() {
        var dateTime = ZonedDateTime.now();
        var firstTransaction = generateTransaction(Long.MAX_VALUE, dateTime, BigDecimal.ONE);
        var secondTransaction = generateTransaction(Long.MAX_VALUE, dateTime, BigDecimal.TEN);

        repository.updateTransaction(firstTransaction);
        repository.updateTransaction(secondTransaction);

        assertFalse(containsAmount(secondTransaction.getAmount()));
    }

    @Test
    void shouldUpdateTransaction() {
        var transaction = generateTransaction();

        repository.updateTransaction(transaction);

        assertFalse(repository.getTransactions().isEmpty());
    }

    @Test
    void shouldUpdateTransactionIfAlreadyExists() {
        var previous = ZonedDateTime.now();
        var later = previous.plusNanos(LONG_ONE);
        var firstTransaction = generateTransaction(Long.MAX_VALUE, previous, BigDecimal.ONE);
        var secondTransaction = generateTransaction(Long.MAX_VALUE, later, BigDecimal.TEN);

        repository.updateTransaction(firstTransaction);
        repository.updateTransaction(secondTransaction);

        assertTrue(containsAmount(secondTransaction.getAmount()));
    }

    private boolean containsAmount(BigDecimal amount) {
        return repository.getTransactions()
                .stream()
                .anyMatch(transaction -> BigDecimal.TEN.equals(transaction.getAmount()));
    }
}
