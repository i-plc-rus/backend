package com.bank.transactions.util;

import com.bank.transactions.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public final class TransactionBatchManager {

    private final List<Transaction> batchList = new ArrayList<>();
    private static final int BATCH_SIZE = 100;
    private final AtomicInteger batchSize = new AtomicInteger(0);

    /**
     * Добавляет транзакцию в текущий пакет.
     *
     * @param transaction транзакция для добавления в пакет
     * <p>
     * Метод увеличивает счетчик размера пакета атомарно,
     * обеспечивая потокобезопасность операции.
     * </p>
     */
    public void addToBatch(Transaction transaction) {
        this.batchList.add(transaction);
        this.batchSize.incrementAndGet();
    }

    /**
     * Проверяет, заполнен ли текущий пакет.
     *
     * @return true, если пакет заполнен, иначе false
     * <p>
     * Метод использует атомарную операцию чтения, что обеспечивает
     * потокобезопасность и высокую производительность.
     * </p>
     */
    public boolean isBatchFull() {
        return batchSize.get() >= BATCH_SIZE;
    }

    /**
     * Очищает текущий пакет и возвращает список транзакций для обработки.
     *
     * @return список транзакций из текущего пакета
     * <p>
     * Метод создает новый список для возврата, чтобы избежать проблем с
     * параллельным доступом. Затем очищает внутренний список и сбрасывает счетчик.
     * </p>
     */
    public List<Transaction> flushBatch() {
        List<Transaction> batchToFlush = new ArrayList<>(batchList);
        this.batchList.clear();
        this.batchSize.set(0);
        return batchToFlush;
    }

    /**
     * Проверяет, пуст ли текущий пакет.
     *
     * @return true, если пакет пуст, иначе false
     * <p>
     * Метод использует встроенный метод isEmpty() списка ArrayList,
     * что обеспечивает константное время выполнения.
     * </p>
     */
    public boolean isBatchEmpty() {
        return batchList.isEmpty();
    }
}
