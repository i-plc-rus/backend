package com.example.transaction.service;

import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.logger.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionProcessor {
    private final TransactionRepository transactionRepository;
    private final LogService logService;

    @Autowired
    public TransactionProcessor(TransactionRepository transactionRepository, LogService logService) {
        this.transactionRepository = transactionRepository;
        this.logService = logService;
    }

    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            try {
                // Логика обработки транзакций
                transactionRepository.save(transaction);
                logService.logInfo("Transaction processed: " + transaction.getId());
            } catch (Exception e) {
                logService.logError("Error processing transaction: " + transaction.getId(), e);
            }
        }
    }
}
