
package com.bank.transactions.old;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OldTransactionProcessorTest {

    @Autowired
    private OldTransactionProcessor processor;

    @Test
    public void testProcessTransactions() {
        List<OldTransaction> transactions = new ArrayList<>();
        transactions.add(new OldTransaction("1", 5000, "2023-01-01", "PENDING"));
        transactions.add(new OldTransaction("2", 15000, "2023-01-02", "PENDING"));
        transactions.add(new OldTransaction("3", 2000, "2023-01-03", "COMPLETED"));

        processor.processTransactions(transactions);

        for (OldTransaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getId() + " Status: " + transaction.getStatus());
        }
    }
}
