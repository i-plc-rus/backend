package com.bank.transactions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionProcessorTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private Logger logger;

    @InjectMocks
    private TransactionProcessor processor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("1", new BigDecimal("5000"), "2023-01-01", "PENDING"));
        transactions.add(new Transaction("2", new BigDecimal("15000"), "2023-01-02", "PENDING"));
        transactions.add(new Transaction("3", new BigDecimal("2000"), "2023-01-03", "COMPLETED"));

        processor.processTransactions(transactions);

        verify(logger).log("Processing large transaction: 2");
        verify(repository).updateTransaction(transactions.get(0));
        verify(repository).updateTransaction(transactions.get(1));
        verify(repository).updateTransaction(transactions.get(2));

        assertThat(transactions.get(0).getStatus()).isEqualTo("PROCESSED");
        assertThat(transactions.get(1).getStatus()).isEqualTo("PROCESSED");
        assertThat(transactions.get(2).getStatus()).isEqualTo("COMPLETED");
    }
}
