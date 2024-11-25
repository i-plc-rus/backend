package com.bank.transactions.refactored;

/// Перечисление состояний транзакции для повышения типобезопасности
public enum TransactionStatus {
    PENDING, PROCESSED, COMPLETED, CANCELLED
}
