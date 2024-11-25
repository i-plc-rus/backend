package com.bank.transactions.transactionprocessor.service;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionProcessingServiceTest {

    @Mock
    private TransactionProcessor transactionProcessor;

    @InjectMocks
    private TransactionProcessingService transactionProcessingService;

    private TransactionDto createTransaction(TransactionStatus status) {
        return TransactionDto.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.TEN)
                .business_date(LocalDateTime.now())
                .status(status)
                .build();
    }

    @Test
    void shouldProcessTransactionsSuccessfully() {
        List<TransactionDto> transactions = List.of(createTransaction(TransactionStatus.PENDING),
                createTransaction(TransactionStatus.PENDING));
        transactionProcessingService.processTransactions(transactions);
        verify(transactionProcessor, times(transactions.size())).processTransaction(any(TransactionDto.class));
    }

    @Test
    void shouldHandleEmptyTransactionListGracefully() {
        transactionProcessingService.processTransactions(List.of());
        verify(transactionProcessor, never()).processTransaction(any());
    }
}