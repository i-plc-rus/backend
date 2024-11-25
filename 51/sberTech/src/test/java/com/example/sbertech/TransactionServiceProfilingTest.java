package com.example.sbertech;

import com.example.sbertech.dao.TransactionRepositoryJunior;
import com.example.sbertech.dao.TransactionRepositoryMine;
import com.example.sbertech.dao.TransactionRepositoryMineNotThreadSafe;
import com.example.sbertech.log.Logger;
import com.example.sbertech.pojo.Transaction;
import com.example.sbertech.service.TransactionService;
import com.example.sbertech.service.TransactionServiceJunior;
import com.example.sbertech.service.TransactionServiceMine;
import com.example.sbertech.service.TransactionServiceMineNotThreadSafe;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class TransactionServiceProfilingTest {

    private static final int TRANSACTION_LIST_SIZE = 1_000_000;
    protected static double WHAT_PART_OF_LIST_WARMS_JVM = 0.1;
    private TransactionServiceJunior serviceJunior;
    private TransactionServiceMine serviceMine;
    private TransactionServiceMineNotThreadSafe serviceMineNotThreadSafe;
    @Autowired
    private TransactionsGenerator tGenerator;

    @BeforeEach
    //тут Spring не уместен, поскольку нужен не синглтон, а прототайп. Но по бизнес-логике классы не прототайпы.
    //Так что создаем через new
    public void before() {
        serviceJunior = new TransactionServiceJunior(new TransactionRepositoryJunior(), new Logger());
        serviceMine = new TransactionServiceMine(new TransactionRepositoryMine());
        serviceMineNotThreadSafe = new TransactionServiceMineNotThreadSafe(
                new TransactionRepositoryMineNotThreadSafe());
    }

    @Test
    public void juniorCodeProfiling() {
        List<Transaction> transactions = tGenerator.getJuniorTransactions(TRANSACTION_LIST_SIZE);
        processTransactionsPerformance(transactions, serviceJunior);

    }

    @Test
    public void myCodeProfilingNotThreadSafe() {
        List<Transaction> transactions = tGenerator.getMyTransactions(TRANSACTION_LIST_SIZE);
        processTransactionsPerformance(transactions, serviceMineNotThreadSafe);
    }

    @Test
    public void myCodeProfiling() {
        List<Transaction> transactions = tGenerator.getMyTransactions(TRANSACTION_LIST_SIZE);
        processTransactionsPerformance(transactions, serviceMine);
    }

    public void processTransactionsPerformance(List<Transaction> transactions,
                                               TransactionService tService) {
        long sizeOfCollectionForWarming = (long) (transactions.size() * WHAT_PART_OF_LIST_WARMS_JVM);
        List<Transaction> firstPart = tGenerator.getFirstPart(sizeOfCollectionForWarming, transactions);
        List<Transaction> lastPart = tGenerator.getLastPart(sizeOfCollectionForWarming, transactions);
        warm(firstPart, tService);
        profile(lastPart, tService);
    }

    private void warm(List<Transaction> firstPart, TransactionService tService) {
        tService.processTransactions(firstPart);
    }

    private void profile(List<Transaction> lastPart, TransactionService tService) {
        long start = System.nanoTime();
        process(lastPart, tService);
        long end = System.nanoTime();
        writeToLog(start, end, lastPart.get(0).getClass(), tService.getClass());
        tGenerator.assertAllTransactionsGeneratedByThisClassAreAdded(tService, TRANSACTION_LIST_SIZE);
    }

    private void process(List<Transaction> lastPart, TransactionService tService) {
        tService.processTransactions(lastPart);
    }

    private void writeToLog(long start, long end, Class<? extends Transaction> transClass,
                            Class<? extends TransactionService> tServiceClass) {
        long resultTime = (end - start) * 10 / TRANSACTION_LIST_SIZE;
        log.info("Result: {} ns for 10 tr. Tested {} and {} for {} transactions.",
                resultTime, transClass.getSimpleName(), tServiceClass.getSimpleName(), TRANSACTION_LIST_SIZE);
    }
}
