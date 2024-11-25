package ru.competition.transactions.model.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransaction;

public class TransactionTest {

    @Test
    void shouldReturnIsNotLargeAmount() {
        assertFalse(generateTransaction(BigDecimal.TEN).isLarge(null));
    }

    @ParameterizedTest
    @MethodSource("getTransactions")
    void testIsNotLargeAmount(Transaction transaction) {
        assertFalse(transaction.isLarge(BigDecimal.TEN));
    }

    @ParameterizedTest
    @MethodSource("getTransactionPairs")
    void testNotEquals(Transaction transaction, Transaction anotherTransaction) {
        assertNotEquals(transaction, anotherTransaction);
    }

    private static Stream<Transaction> getTransactions() {
        return Stream.of(
                generateTransaction(Long.MAX_VALUE),
                generateTransaction(BigDecimal.TEN)
        );
    }

    private static Stream<Arguments> getTransactionPairs() {
        return Stream.of(
                arguments(generateTransaction(Long.MAX_VALUE), generateTransaction(BigDecimal.TEN)),
                arguments(generateTransaction(BigDecimal.TEN), generateTransaction(Long.MAX_VALUE)),
                arguments(generateTransaction(Long.MAX_VALUE), generateTransaction(Long.MIN_VALUE))
        );
    }
}
