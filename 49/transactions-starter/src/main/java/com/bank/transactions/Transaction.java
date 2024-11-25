package com.bank.transactions;

/**
 * Транзакция
 */
public interface Transaction {

    /**
     * @return Идентификатор
     */
    String getId();

    /**
     * @return Сумма
     */
    Double getAmount();

    /**
     * @return Дата
     */
    String getDate();

    /**
     * @return Статус
     */
    String getStatus();

}
