package com.bank.transactions.repo;

import com.bank.transactions.model.Transaction;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public final class TransactionRepositoryImpl implements TransactionRepository {

    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    /**
     * Открывает новую сессию для работы с транзакциями.
     *
     * @return новый экземпляр TransactionSession
     * <p>
     * Использование паттерна "Сессия" позволяет группировать операции
     * и обеспечивает атомарность при обновлении нескольких транзакций.
     * </p>
     */
    @Override
    public @NonNull TransactionSession openSession() {
        return new TransactionSessionImpl();
    }

    private class TransactionSessionImpl implements TransactionSession {

        /** Синхронизированный список для хранения транзакций в рамках сессии. */
        private final List<Transaction> sessionTransactions
                = Collections.synchronizedList(new ArrayList<>());

        /**
         * Обновляет транзакции в рамках текущей сессии.
         *
         * @param newTransactions список новых транзакций для обновления
         * <p>
         * Метод добавляет новые транзакции в список сессии.
         * Синхронизированный список обеспечивает потокобезопасность операции.
         * </p>
         */
        @Override
        public void updateTransactions(List<Transaction> newTransactions) {
            this.sessionTransactions.addAll(newTransactions);
        }

        /**
         * Закрывает текущую сессию, применяя все изменения.
         * <p>
         * Метод добавляет все транзакции из сессии в основной список транзакций.
         * CopyOnWriteArrayList обеспечивает потокобезопасность при добавлении.
         * После применения изменений, список сессии очищается.
         * </p>
         */
        @Override
        public void close() {
            transactions.addAll(sessionTransactions);
            sessionTransactions.clear();
        }
    }

    /**
     * Возвращает неизменяемое представление списка всех транзакций.
     *
     * @return неизменяемый список всех транзакций
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}