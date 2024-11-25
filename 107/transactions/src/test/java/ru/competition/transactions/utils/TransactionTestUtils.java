package ru.competition.transactions.utils;

import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.competition.transactions.utils.CommonConstants.INT_ZERO;
import static ru.competition.transactions.utils.ExceptionUtils.throwCannotBeInitialized;
import static ru.competition.transactions.utils.RandomUtils.randomBigDecimal;
import static ru.competition.transactions.utils.RandomUtils.randomLong;
import static ru.competition.transactions.utils.RandomUtils.randomStatus;
import static ru.competition.transactions.utils.RandomUtils.randomZonedDateTime;

public class TransactionTestUtils {

    private TransactionTestUtils() {
        throwCannotBeInitialized(this.getClass().getName());
    }

    public static Transaction generateTransaction() {
        return new Transaction.Builder()
                .id(randomLong())
                .amount(randomBigDecimal())
                .date(randomZonedDateTime())
                .status(randomStatus())
                .build();
    }

    public static Transaction generateTransaction(BigDecimal amount) {
        return new Transaction.Builder()
                .amount(amount)
                .build();
    }

    public static Transaction generateTransaction(Long id) {
        return new Transaction.Builder()
                .id(randomLong())
                .build();
    }

    public static Transaction generateTransaction(ZonedDateTime date) {
        return new Transaction.Builder()
                .date(date)
                .build();
    }

    public static Transaction generateTransaction(TransactionStatus status) {
        return new Transaction.Builder()
                .id(randomLong())
                .amount(randomBigDecimal())
                .date(randomZonedDateTime())
                .status(status)
                .build();
    }

    public static Transaction generateTransaction(Long id, ZonedDateTime dateTime, BigDecimal amount) {
        return new Transaction.Builder()
                .id(id)
                .amount(amount)
                .date(dateTime)
                .status(randomStatus())
                .build();
    }

    public static List<Transaction> generateTransactions(int size) {
        List<Transaction> transactions = new ArrayList<>(size);
        for (int i = INT_ZERO; i < size; i++) {
            transactions.add(generateTransaction());
        }
        return transactions;
    }
}
