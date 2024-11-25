package com.bank.transactions.app.models;

/**
 * Представляет финансовую транзакцию с ID, размером, датой и статусом
 */
public class Transaction {
    private final String id;
    private final double amount;
    private final String date;
    private String status;

    /**
     * Конструктор транзакции с определенными параметрами
     *
     * @param id     уникальный идентификатор транзакции
     * @param amount количество денег в транзакции
     * @param date   дата транзакции
     * @param status текущее состояние транзакции
     */
    public Transaction(String id, double amount, String date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    /**
     * Возвращает уникальный идентификатор транзакции
     *
     * @return ID транзакции
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращает количество денег в транзакции
     *
     * @return размер транзакции
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Возвращает дату транзакции
     *
     * @return дата транзакции
     */
    public String getDate() {
        return date;
    }

    /**
     * Возвращает текущий статус транзакции
     *
     * @return статус транзакции
     */
    public String getStatus() {
        return status;
    }

    /**
     * Обновляет статус транзакции
     *
     * @param status новый статус транзакции
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Возвращает строковое представление транзакции
     *
     * @return строка содержащая детали транзакции
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
