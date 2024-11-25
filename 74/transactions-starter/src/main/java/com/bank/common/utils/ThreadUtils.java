package com.bank.common.utils;

import java.util.concurrent.ForkJoinPool;

/**
 * Утилита для работы с потоками
 */
public class ThreadUtils {

    /**
     * Конструктор утилиты недоступен потребителям
     */
    private ThreadUtils() {
    }

    public static int parallelism() {
        return ForkJoinPool.getCommonPoolParallelism();
    }

    /**
     * Приостанавливает выполнение текущего потока на 1 миллисекунду
     */
    @Deprecated
    public static void sleep1Ms() {
        ThreadUtils.sleep(1);
    }

    /**
     * Приостанавливает выполнение текущего потока на 10 миллисекунд
     */
    @Deprecated
    public static void sleep10Ms() {
        ThreadUtils.sleep(10);
    }

    /**
     * Приостанавливает выполнение текущего потока на заданное время
     *
     * @param millis время в миллисекундах
     */
    @Deprecated
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
