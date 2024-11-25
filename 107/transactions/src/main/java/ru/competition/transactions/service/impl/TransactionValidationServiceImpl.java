package ru.competition.transactions.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.service.TransactionValidationService;
import ru.competition.transactions.service.TransactionValidator;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionValidationServiceImpl implements TransactionValidationService {
    private final TransactionValidator validator;

    @Autowired
    public TransactionValidationServiceImpl(TransactionValidator validator) {
        this.validator = validator;
    }

    @Override
    public List<Transaction> validate(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Objects::nonNull)
                .filter(validator::isValid)
                .toList();
    }
}
