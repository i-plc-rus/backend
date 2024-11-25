package com.bank.transactions;

import java.util.List;

/**
 * Обработчик транзакций
 */
public interface TransactionProcessor {

    /**
     * Обрабатывает транзакции
     *
     * @param transactions Транзакции
     */
    void processTransactions(List<Transaction> transactions);

}
