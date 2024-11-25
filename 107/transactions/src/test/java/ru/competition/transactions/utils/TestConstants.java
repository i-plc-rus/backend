package ru.competition.transactions.utils;

import static ru.competition.transactions.utils.ExceptionUtils.throwCannotBeInitialized;

public class TestConstants {
    public static final long LONG_ONE = 1L;

    private TestConstants() {
        throwCannotBeInitialized(this.getClass().getName());
    }
}
