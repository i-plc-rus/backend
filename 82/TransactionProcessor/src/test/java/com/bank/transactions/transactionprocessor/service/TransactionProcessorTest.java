package com.bank.transactions.transactionprocessor.service;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import com.bank.transactions.transactionprocessor.exception.ProcessorNotFoundException;
import com.bank.transactions.transactionprocessor.service.processor.TransactionProcessorStrategy;
import com.bank.transactions.transactionprocessor.service.processor.TransactionTypeResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionProcessorTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionTypeResolver typeResolver;

    @Mock
    private Map<TransactionType, TransactionProcessorStrategy> processors;

    @Mock
    private TransactionProcessorStrategy transactionProcessorStrategy;

    private TransactionProcessor transactionProcessor;

    private TransactionType transactionType;

    private TransactionDto createTransaction(TransactionStatus status) {
        return TransactionDto.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.TEN)
                .business_date(LocalDateTime.now())
                .status(status)
                .build();
    }

    @BeforeEach
    void setUp() {
        transactionType = mock(TransactionType.class);
        when(transactionProcessorStrategy.getTransactionType()).thenReturn(transactionType);
        transactionProcessor = new TransactionProcessor(
                transactionService, typeResolver, List.of(transactionProcessorStrategy)
        );
    }

    @Test
    void shouldProcessPendingTransactionSuccessfully() {
        TransactionDto transaction = createTransaction(TransactionStatus.PENDING);
        when(transactionService.isTransactionProcessed(transaction.id())).thenReturn(false);
        when(typeResolver.getType(transaction)).thenReturn(transactionType);

        transactionProcessor.processTransaction(transaction);

        verify(transactionProcessorStrategy).processTransaction(transaction);
    }

    @Test
    void shouldNotReprocessCompletedTransaction() {
        TransactionDto transaction = createTransaction(TransactionStatus.COMPLETED);
        transactionProcessor.processTransaction(transaction);
        verify(transactionService, times(1)).completeTransaction(transaction.id());
    }

    @Test
    void shouldHandleUnknownTransactionType() {
        TransactionDto transaction = createTransaction(TransactionStatus.PENDING);
        when(typeResolver.getType(transaction)).thenReturn(TransactionType.UNDEFINED);
        assertThrows(ProcessorNotFoundException.class, () -> transactionProcessor.processTransaction(transaction));
    }

    @Test
    void shouldSaveFailedTransactionOnException() {
        TransactionDto transaction = createTransaction(TransactionStatus.PENDING);
        when(typeResolver.getType(transaction)).thenReturn(transactionType);
        doThrow(new RuntimeException()).when(transactionProcessorStrategy).processTransaction(transaction);

        assertThrows(RuntimeException.class, () -> transactionProcessor.processTransaction(transaction));
        verify(transactionService).saveFailedTransaction(transaction);
    }
}
