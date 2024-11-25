package com.bank.transactions.service.impl.batch;

import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionBatchProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.bank.transactions.data.model.TransactionStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionBatchConcurrentProcessorImplTest {

    @Autowired
    private TransactionBatchProcessor transactionBatchProcessor;

    @Test
    void testProcessTransactions() throws InterruptedException {
        List<Transaction> transactions = new ArrayList<>();

        String firstId = "1";
        String secondId = "2";
        String thirdId = "3";

        transactions.add(new Transaction(firstId, BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), PENDING));
        transactions.add(new Transaction(secondId, BigDecimal.valueOf(15000), LocalDate.parse("2023-01-02"), PENDING));
        transactions.add(new Transaction(thirdId, BigDecimal.valueOf(2000), LocalDate.parse("2023-01-03"), COMPLETED));

        transactionBatchProcessor.processTransactions(transactions);

        long startTime = System.currentTimeMillis();
        long timeoutInMillis = 2000;
        while (!transactions.get(0).isProcessed() && System.currentTimeMillis() - startTime < timeoutInMillis) {
            Thread.sleep(500);
        }

        assertEquals(PROCESSED, transactions.get(0).getStatus());
        assertEquals(PROCESSED, transactions.get(1).getStatus());
        assertEquals(COMPLETED, transactions.get(2).getStatus());
    }
}