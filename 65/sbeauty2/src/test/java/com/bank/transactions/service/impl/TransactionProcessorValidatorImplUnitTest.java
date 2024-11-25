package com.bank.transactions.service.impl;

import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionProcessorValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.bank.transactions.data.model.TransactionStatus.COMPLETED;
import static com.bank.transactions.data.model.TransactionStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionProcessorValidatorImplUnitTest {

    private final TransactionProcessorValidator validator = new TransactionProcessorValidatorImpl();

    @Test
    void shouldValidateForProcessingIfTransactionIsNotPending() {
        assertFalse(
                validator.validateForProcessing(
                        new Transaction("3", BigDecimal.valueOf(2000), LocalDate.parse("2023-01-03"), COMPLETED)
                )
        );
    }

    @Test
    void shouldNotValidateForProcessingIfTransactionIsPending() {
        assertTrue(
                validator.validateForProcessing(
                        new Transaction("3", BigDecimal.valueOf(2000), LocalDate.parse("2023-01-03"), PENDING)
                )
        );
    }
}