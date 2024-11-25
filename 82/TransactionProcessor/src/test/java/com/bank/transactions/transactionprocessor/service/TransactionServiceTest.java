package com.bank.transactions.transactionprocessor.service;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import com.bank.transactions.transactionprocessor.repository.TransactionRepository;
import com.bank.transactions.transactionprocessor.repository.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionDto createTransaction(TransactionStatus status) {
        return TransactionDto.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.TEN)
                .business_date(LocalDateTime.now())
                .status(status)
                .build();
    }

    @Test
    void shouldSaveTransactionSuccessfully() {
        TransactionDto transaction = createTransaction(TransactionStatus.PENDING);
        transactionService.save(transaction, TransactionStatus.PENDING, TransactionType.LARGE);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldMarkTransactionAsProcessed() {
        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.PROCESSED);
        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        boolean isProcessed = transactionService.isTransactionProcessed(UUID.randomUUID());
        assertTrue(isProcessed);
    }

    @Test
    void shouldSaveFailedTransaction() {
        TransactionDto transaction = createTransaction(TransactionStatus.FAILED);
        transactionService.saveFailedTransaction(transaction);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldDeleteTransactionOnComplete() {
        UUID id = UUID.randomUUID();
        transactionService.completeTransaction(id);
        verify(transactionRepository).deleteById(id);
    }
}