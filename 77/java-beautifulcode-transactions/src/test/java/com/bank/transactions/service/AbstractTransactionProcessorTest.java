package com.bank.transactions.service;

import com.bank.transactions.model.Transaction;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


abstract class AbstractTransactionProcessorTest {
    private static final Random random = new Random();
    private final TransactionProcessor service;

    AbstractTransactionProcessorTest(TransactionProcessor service) {
        this.service = service;
    }

    @Test
    public void whenAddTransactionsWithZeroArgument_thenThrowExecutionException() {
        assertThrows(Exception.class,
                () -> service.addTransaction(0));
    }

    @Test
    public void addTransactions() {
        int countToAdd = 1000;
        int transactionsCountBeforeAdd = service.getTransactionsCount();

        for (int i = 0; i < countToAdd; i++) {
            service.addTransaction(random.nextDouble(-999999, 999999));
        }
        int transactionsCountAfter = service.getTransactionsCount();

        assertEquals(transactionsCountAfter - transactionsCountBeforeAdd, countToAdd);
    }

    @Test
    void whenTransactionAmountGreaterOrEqualsAuditableSum_thenProcessingLargeTransactionIsPrinted() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalSystemOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // контролируемая сумма определена в application.properties или в аннотации @AmountAuditable
        service.addTransaction(600_000);

        System.setOut(originalSystemOut);
        String logOutput = outputStream.toString();
        // содержание лог-сообщения определено в классе TransactionMonitoringAspect
        assertThat(logOutput).contains("Processing large transaction amount");
    }

    @Test
    public void processTransactions() throws ExecutionException, InterruptedException {
        int count = 1000;
        List<Long> createdTds = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            CompletableFuture<Transaction> transaction = service.addTransaction(random.nextDouble(-9999, 9999));
            createdTds.add(transaction.get().getId());
        }
        service.processTransactions(createdTds);

        for (long id : createdTds) {
            assertEquals(service.getTransaction(id).getStatus(), Transaction.Status.PROCESSED);
        }
    }

    @Test
    public void whenProcessTransactionsWithEmptyArgument_thenThrowExecutionException() {
        assertThrows(Exception.class,
                () -> service.processTransactions(new ArrayList<>()));
    }
}
