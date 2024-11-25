package ru.competition.transactions.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.repository.TransactionRepository;
import ru.competition.transactions.repository.TransactionRepositoryFactory;

import java.util.List;

@Service
public class TransactionRepositoryFactoryImpl implements TransactionRepositoryFactory {
    private static final String REPOSITORY_NOT_FOUND = "TransactionRepository with type: %s not found";

    private final List<TransactionRepository> repositories;

    @Autowired
    public TransactionRepositoryFactoryImpl(List<TransactionRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public TransactionRepository getTransactionRepository(ProcessorType processorType) {
        return repositories.stream()
                .filter(repository -> repository.isApplicable(processorType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(REPOSITORY_NOT_FOUND, processorType)));
    }
}
