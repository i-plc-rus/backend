package com.bank.common.concurent;

import com.bank.common.utils.ThreadUtils;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

/**
 * Позволяет обрабатывать массив данных параллельно в несколько потоков
 *
 * @param <T> тип данных
 */
public class ArrayRecursiveAction<T> extends RecursiveAction {

    private final T[] items;
    private final Consumer<T> action;
    private final int threshold;
    private final int indexFrom;
    private final int indexTo;

    /**
     * Публичный конструктор
     *
     * @param items массив данных
     * @param action действие, выполняемое над элементами массива данных
     */
    public ArrayRecursiveAction(T[] items, Consumer<T> action) {
        this.items = items;
        this.action = action;
        this.threshold = threshold(items);
        this.indexFrom = 0;
        this.indexTo = items.length;
    }

    /**
     * Приватный конструктор для создания рекурсий
     *
     * @param items исходный массив данных
     * @param action действие, выполняемое над элементами массива
     * @param threshold значение отсечки для прекращения рекурсии
     * @param indexFrom начальный индекс массива для данной рекурсии
     * @param indexTo конечный индекс массива для данной рекурсии
     */
    private ArrayRecursiveAction(T[] items, Consumer<T> action, int threshold, int indexFrom, int indexTo) {
        this.items = items;
        this.action = action;
        this.threshold = threshold;
        this.indexFrom = indexFrom;
        this.indexTo = indexTo;
    }

    /**
     * Запуск параллельной обработки массива данных
     */
    public void forkJoin() {
        ForkJoinTask.invokeAll(this);
    }

    @Override
    protected void compute() {
        // если количество элементов данных больше, чем значение отсечки,
        // то разделяем текущую рекурсию на две новых рекурсии,
        // иначе обработаем данные текущей рекурсии
        if (length() > threshold) {
            ForkJoinTask.invokeAll(
                new ArrayRecursiveAction<>(items, action, threshold, indexFrom, indexFrom + length() / 2),
                new ArrayRecursiveAction<>(items, action, threshold, indexFrom + length() / 2, indexTo)
            );
        } else {
            for (int i = indexFrom; i < indexTo; i++) {
                action.accept(items[i]);
            }
        }
    }

    /**
     * Вычисляет значение отсечки для прекращения рекурсии
     *
     * @param items исходный массив данных
     * @return значение отсечки
     */
    private int threshold(T[] items) {
        return 1 + items.length / ThreadUtils.parallelism() / 2;
    }

    /**
     * Возвращает количество элементов для обработки
     *
     * @return количество элементов для обработки
     */
    private int length() {
        return indexTo - indexFrom;
    }
}
