package com.example.sbertech;

import com.example.sbertech.dao.TransactionRepositoryJunior;
import com.example.sbertech.dao.TransactionRepositoryMine;
import com.example.sbertech.log.Logger;
import com.example.sbertech.pojo.Transaction;
import com.example.sbertech.service.TransactionService;
import com.example.sbertech.service.TransactionServiceJunior;
import com.example.sbertech.service.TransactionServiceMine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TransactionServiceMultithreadingTest {

    private static final int TRANSACTION_LIST_SIZE = 1_000_000;
    private TransactionServiceJunior serviceJunior;
    private TransactionServiceMine serviceMine;
    @Autowired
    private TransactionsGenerator tGenerator;

    @BeforeEach
    //тут Spring не уместен, поскольку нужен не синглтон, а прототайп. Но по бизнес-логике классы не прототайпы.
    //Так что создаем через new
    public void before() {
        serviceJunior = new TransactionServiceJunior(
                new TransactionRepositoryJunior(), new Logger());

        serviceMine = new TransactionServiceMine(new TransactionRepositoryMine());
    }

    @Test
    public void myCodeMultithreading() {
        List<Transaction> transactions = tGenerator.getMyTransactions(TRANSACTION_LIST_SIZE);
        processTransactionsInThreads(transactions, serviceMine);
    }

    //@Test
    //Тест рабочий. А вот код джуниора — нет, потому что не потокобезопасный. Поэтому этот тест и не проходит.
    //Закомментировали на случай, если проверяющий запустит тесты и подумает, отчего это в проекте неработающие тесты.
    public void juniorCodeMultithreading() {
        List<Transaction> transactions = tGenerator.getJuniorTransactions(TRANSACTION_LIST_SIZE);
        processTransactionsInThreads(transactions, serviceJunior);
    }

    private void processTransactionsInThreads(List<Transaction> transactions, TransactionService tService) {
        transactions.parallelStream().forEach(tService::processTransaction);
        tGenerator.assertAllTransactionsGeneratedByThisClassAreAdded(tService, TRANSACTION_LIST_SIZE);
    }
}
