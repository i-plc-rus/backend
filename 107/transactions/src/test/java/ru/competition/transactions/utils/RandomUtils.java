package ru.competition.transactions.utils;

import ru.competition.transactions.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Random;

import static ru.competition.transactions.utils.ExceptionUtils.throwCannotBeInitialized;

public class RandomUtils {
    private static final Random RANDOM = new Random();
    private static final Long RANDOM_LONG_ORIGIN = 0L;

    private static final Long RANDOM_LONG_BOUND = 10L;

    private RandomUtils() {
        throwCannotBeInitialized(this.getClass().getName());
    }

    public static long randomLong() {
        return RANDOM.nextLong(RANDOM_LONG_ORIGIN, RANDOM_LONG_BOUND);
    }

    public static BigDecimal randomBigDecimal() {
        return BigDecimal.valueOf(RANDOM.nextLong());
    }

    public static ZonedDateTime randomZonedDateTime() {
        return ZonedDateTime.now();
    }

    public static TransactionStatus randomStatus() {
        TransactionStatus[] types = TransactionStatus.values();
        return types[RANDOM.nextInt(types.length)];
    }
}
