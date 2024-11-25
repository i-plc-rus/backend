
package com.bank.transactions;

import com.bank.transactions.dao.TransactionRepository;
import com.bank.transactions.dto.Transaction;
import com.bank.transactions.service.TransactionProcessor;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionProcessorTest {
    @Autowired
    private TransactionProcessor processor;
    @SpyBean
    private TransactionRepository repository;

    @Test
    @DisplayName("Проверяем что обновлялись только транзакции со статусом \"PENDING\"")
    public void testProcessTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("1", 5000, "2023-01-01", "PENDING"));
        transactions.add(new Transaction("2", 15000, "2023-01-02", "PENDING"));
        transactions.add(new Transaction("3", 2000, "2023-01-03", "COMPLETED"));

        processor.processTransactions(transactions);

        verify(repository, times(2)).updateTransaction(any(Transaction.class));

        List<Transaction> savedTransactions = repository.getTransactions();

        assertAll(
                () -> assertEquals(2, savedTransactions.size()),
                () -> assertEquals("PROCESSED", savedTransactions.getFirst().getStatus()),
                () -> assertEquals("PROCESSED", savedTransactions.getLast().getStatus())
        );
    }

    @Test
    @DisplayName("Проверяем что если нет транзакций со статусом \"PENDING\" их обновление не запускается")
    public void testProcessTransactionsForCompleted() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("1", 5000, "2023-01-01", "COMPLETED"));
        transactions.add(new Transaction("2", 15000, "2023-01-02", "COMPLETED"));
        transactions.add(new Transaction("3", 2000, "2023-01-03", "COMPLETED"));

        processor.processTransactions(transactions);

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Проверяем, что если передать null, ничего не ломается")
    public void testProcessTransactionsForNullTransactions() {
        assertDoesNotThrow(() -> processor.processTransactions(null));
    }
}
