package ru.competition.transactions.repository;

import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.ProcessorStrategy;

import java.util.List;

public interface TransactionRepository extends ProcessorStrategy {

    /**
     * Метод для обновления транзакции.
     * <p>Если существует транзакция с таким же id, и она произошла раньше новой транзакции,
     * то обновляем существующую транзакцию на новую.</p>
     * <p>Если транзакция с таким же id не существует,
     * то добавляем новую транзакцию.</p>
     *
     * @param transaction новая транзакция
     */
    void updateTransaction(Transaction transaction);

    List<Transaction> getTransactions();

    void deleteAll(ProcessorType processorType);
}
