package com.bank.transactions.service.impl;

import com.bank.transactions.data.dao.TransactionRepository;
import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionProcessor;
import com.bank.transactions.service.TransactionProcessorValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.bank.transactions.data.model.TransactionStatus.ERROR;
import static com.bank.transactions.data.model.TransactionStatus.PROCESSED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionProcessorImplUnitTest {
    private final TransactionRepository repositoryMock = mock(TransactionRepository.class);
    private final TransactionProcessorValidator validatorMock = mock(TransactionProcessorValidator.class);

    private final TransactionProcessor transactionProcessor = new TransactionProcessorImpl(repositoryMock, validatorMock);

    @Test
    void checkNullTransactionProcess() {
        assertDoesNotThrow(() -> transactionProcessor.processTransaction(null));
    }

    @Test
    void checkRepositoryIsNotCalledInFailedValidationCase() {
        when(validatorMock.validateForProcessing(any()))
                .thenReturn(false);

        Transaction transaction = new Transaction("", BigDecimal.valueOf(0), LocalDate.now(), PROCESSED);
        transactionProcessor.processTransaction(transaction);

        verify(repositoryMock, never())
                .updateTransaction(transaction);
    }

    @Test
    void checkRepositoryCallInSuccessValidationCase() {
        when(validatorMock.validateForProcessing(any()))
                .thenReturn(true);

        Transaction transaction = new Transaction("", BigDecimal.valueOf(0), LocalDate.now(), PROCESSED);
        transactionProcessor.processTransaction(transaction);

        verify(repositoryMock)
                .updateTransaction(transaction);
        assertEquals(PROCESSED, transaction.getStatus());
    }

    @Test
    void checkTransactionIsInErrorStateAfterProcessError() {
        when(validatorMock.validateForProcessing(any()))
                .thenReturn(true);
        doThrow(new RuntimeException())
                .when(repositoryMock)
                .updateTransaction(any());

        Transaction transaction = new Transaction("", BigDecimal.valueOf(0), LocalDate.now(), PROCESSED);
        transactionProcessor.processTransaction(transaction);

        verify(repositoryMock)
                .updateTransaction(transaction);
        assertEquals(ERROR, transaction.getStatus());
    }
}