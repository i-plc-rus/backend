package com.bank.common.utils;

/**
 * Утилита для работы с потоками
 */
public class ThreadUtils {

    /**
     * Конструктор утилиты недоступен потребителям
     */
    private ThreadUtils() {
    }

    /**
     * Приостанавливает выполнение текущего потока на 10 миллисекунд
     */
    public static void sleep10Ms() {
        ThreadUtils.sleep(10);
    }

    /**
     * Приостанавливает выполнение текущего потока на заданное время
     *
     * @param millis время в миллисекундах
     */
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
