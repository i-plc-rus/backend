package ru.competition.transactions.utils;

import static ru.competition.transactions.utils.ExceptionUtils.throwCannotBeInitialized;

public class CommonConstants {
    public static final int INT_ZERO = 0;

    private CommonConstants() {
        throwCannotBeInitialized(this.getClass().getName());
    }
}
