package com.example.sbertech;

import com.example.sbertech.dao.TransactionRepositoryMine;
import com.example.sbertech.pojo.Transaction;
import com.example.sbertech.service.TransactionService;
import com.example.sbertech.service.TransactionServiceMine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Small tests for small cases
 */
@SpringBootTest
public class TransactionServiceTest {

    private static final int TRANSACTION_LIST_SIZE = 10;
    private TransactionService service;
    @Autowired
    private TransactionsGenerator tGenerator;

    @BeforeEach
    //тут Spring не уместен, поскольку нужен не синглтон, а прототайп. Но по бизнес-логике классы не прототайпы.
    //Так что создаем через new
    public void before() {
        service = new TransactionServiceMine(new TransactionRepositoryMine());
    }

    @Test
    public void processTransactionsTest() {
        List<Transaction> transactions = tGenerator.getMyTransactions(TRANSACTION_LIST_SIZE);
        service.processTransactions(transactions);
        tGenerator.assertAllTransactionsGeneratedByThisClassAreAdded(service, TRANSACTION_LIST_SIZE);
    }

    @Test //достаточно того, что он не упал с эксепшном
    public void exceptionTest() {
        TransactionRepositoryMine sMock = mock(TransactionRepositoryMine.class);
        doThrow(new IllegalStateException("Error occurred")).when(sMock)
                .updateTransaction(any());
        service = new TransactionServiceMine(sMock);
        List<Transaction> transactions = tGenerator.getMyTransactions(TRANSACTION_LIST_SIZE);
        service.processTransactions(transactions);
    }
}
