package ru.competition.transactions.service;

import ru.competition.transactions.model.dto.Transaction;

public interface TransactionValidator {

    boolean isValid(Transaction transaction);
}
