package com.bank.transactions;

import com.bank.transactions.dto.Transaction;
import com.bank.transactions.legacy.TransactionProcessorLegacyImpl;
import com.bank.transactions.service.TransactionProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionsApplicationTest {
    @Autowired
    private TransactionProcessor processor;
    @Autowired
    private TransactionProcessorLegacyImpl processorLegacy;

    @Test
    @DisplayName("Нагрузочный тест - обработка 100 000 транзакций")
    public void testProcessTransactionsFor100kPendingTransactions() {
        int transactionsCount = 100000;
        List<Transaction> transactions = new ArrayList<>(transactionsCount);
        for (int i = 1; i <= transactionsCount; i++) {
            transactions.add(new Transaction(String.valueOf(i),
                    i % 5 == 0 ? 15000 : 5000, "2023-01-01", i % 3 == 0 ? "COMPLETED" : "PENDING"));
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("processTransactions_legacy");
        processorLegacy.processTransactions(transactions);
        stopWatch.stop();

        transactions = new ArrayList<>(transactionsCount);
        for (int i = 1; i <= transactionsCount; i++) {
            transactions.add(new Transaction(String.valueOf(i),
                    i % 5 == 0 ? 15000 : 5000, "2023-01-01", i % 3 == 0 ? "COMPLETED" : "PENDING"));
        }
        stopWatch.start("processTransactions");
        processor.processTransactions(transactions);
        stopWatch.stop();

        StopWatch.TaskInfo[] taskInfo = stopWatch.getTaskInfo();

        System.out.println("Processing time: " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        assertTrue(taskInfo[0].getTimeMillis() > taskInfo[1].getTimeMillis());
    }
}
