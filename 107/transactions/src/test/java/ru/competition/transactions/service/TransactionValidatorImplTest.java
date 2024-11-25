package ru.competition.transactions.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.service.impl.TransactionValidatorImpl;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransaction;

public class TransactionValidatorImplTest {

    private final TransactionValidator validator = new TransactionValidatorImpl();

    @ParameterizedTest
    @MethodSource("getTransactions")
    void testNotValid(Transaction transaction) {
        assertFalse(validator.isValid(transaction));
    }

    private static Stream<Transaction> getTransactions() {
        return Stream.of(
                generateTransaction(ZonedDateTime.now()),
                generateTransaction(Long.MAX_VALUE)
        );
    }
}
