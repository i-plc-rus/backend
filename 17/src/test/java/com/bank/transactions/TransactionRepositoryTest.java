package com.bank.transactions;

import com.bank.transactions.dao.TransactionRepository;
import com.bank.transactions.dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        List<Transaction> transactions = new ArrayList<>(100);
        for (int i = 1; i <= 100; i++) {
            transactions.add(new Transaction(String.valueOf(i),
                    i % 5 == 0 ? 15000 : 5000, "2023-01-01", "PENDING"));
        }
        repository.saveAll(transactions);
    }

    @Test
    @DisplayName("При обновлении транзакций, которые уже были в репозитории, происходит перезапись")
    public void updateTransactionTest() {
        List<Transaction> transactions = repository.getTransactions();
        transactions.forEach(transaction -> {
            transaction.setStatus("PROCESSED");
            repository.updateTransaction(transaction);
        });

        List<Transaction> updatedTransactions = repository.getTransactions();

        // Проверяем что размер репозитория не изменился
        assertEquals(transactions.size(), updatedTransactions.size());
        // Проверяем что статус транзакций изменился
        assertEquals("PROCESSED", updatedTransactions.getFirst().getStatus());
        assertEquals("PROCESSED", updatedTransactions.getLast().getStatus());
    }
}
