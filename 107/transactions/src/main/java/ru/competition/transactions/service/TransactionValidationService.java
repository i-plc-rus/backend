package ru.competition.transactions.service;

import ru.competition.transactions.model.dto.Transaction;

import java.util.List;

public interface TransactionValidationService {

    List<Transaction> validate(List<Transaction> transactions);
}
