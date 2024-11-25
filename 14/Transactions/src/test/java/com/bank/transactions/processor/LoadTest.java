package com.bank.transactions.processor;

import com.bank.transactions.BaseTest;
import com.bank.transactions.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LoadTest extends BaseTest {

    @Test
    public void testOptimizedAndDefaultProcessing() {
        List<Transaction> transactions = createPendingTransactions(1_000_000);
        long optimizedTestStartTime = System.currentTimeMillis();
        newProcessor.processTransactions(transactions);
        long optimizedProcessingTime = System.currentTimeMillis() - optimizedTestStartTime;

        clearStorage();

        transactions = createPendingTransactions(1_000_000);
        long defaultTestStartTime = System.currentTimeMillis();
        oldProcessor.processTransactions(transactions);
        long defaultProcessingTime = System.currentTimeMillis() - defaultTestStartTime;

        System.out.println(optimizedProcessingTime);
        System.out.println(defaultProcessingTime);

        assertThat(20 * optimizedProcessingTime).isLessThan(defaultProcessingTime)
                .describedAs("Optimized processing at least 20x faster than default one");
    }
}
