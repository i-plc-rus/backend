package com.bank.transactions;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionProcessorTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionProcessor processor;

    @Test
    public void shouldProcessPendingTransactions() {
        MockitoAnnotations.openMocks(this);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("1", 5000, "2023-01-01", "PENDING"));
        transactions.add(new Transaction("2", 15000, "2023-01-02", "PENDING"));
        transactions.add(new Transaction("3", 2000, "2023-01-03", "COMPLETED"));

        processor.processTransactionList(transactions);

        verify(repository, times(2)).save(any(Transaction.class));
    }
}
