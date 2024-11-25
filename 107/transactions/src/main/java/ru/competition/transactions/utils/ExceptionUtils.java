package ru.competition.transactions.utils;

public class ExceptionUtils {
    private static final String CANNOT_BE_INITIALIZED = "%s cannot be initialized";

    private ExceptionUtils() {
        throwCannotBeInitialized(this.getClass().getName());
    }

    public static void throwCannotBeInitialized(String className) {
        throw new UnsupportedOperationException(String.format(CANNOT_BE_INITIALIZED, className));
    }
}
