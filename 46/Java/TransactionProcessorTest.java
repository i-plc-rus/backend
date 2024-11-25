package com.example.transaction;

import com.example.transaction.model.Transaction;
import com.example.transaction.service.TransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class TransactionProcessorTest {
    private TransactionProcessor transactionProcessor;
    private TransactionRepository transactionRepository;
    private LogService logService;

    @BeforeEach
    public void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        logService = mock(LogService.class);
        
        transactionProcessor = new TransactionProcessor(transactionRepository, logService);
    }

    @Test
    public void processTransactions_shouldProcessAllTransactions() {
        Transaction transaction1 = new Transaction(1L, BigDecimal.valueOf(100), LocalDateTime.now(), "NEW");
        Transaction transaction2 = new Transaction(2L, BigDecimal.valueOf(200), LocalDateTime.now(), "NEW");

        transactionProcessor.processTransactions(Arrays.asList(transaction1, transaction2));

        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(logService, times(2)).logInfo(anyString());
    }

    @Test
    public void processTransactions_shouldHandleExceptions() {
        Transaction failingTransaction = new Transaction(3L, BigDecimal.valueOf(300), LocalDateTime.now(), "NEW");
        doThrow(new RuntimeException("Error")).when(transactionRepository).save(any(Transaction.class));

        transactionProcessor.processTransactions(Arrays.asList(failingTransaction));

        verify(logService, times(1)).logError(contains("Error processing transaction"), any(Exception.class));
    }
}
