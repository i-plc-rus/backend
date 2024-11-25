
package ru.competition.transactions.IT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.competition.transactions.model.dto.Transaction;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.TransactionFacade;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransaction;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransactions;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionProcessorIT {
    private static final int TRANSACTION_SIZE = 100000;

    @Autowired
    private TransactionFacade transactionFacade;

    @Test
    public void comparingTimeProcessTransactions() {
        var concurrentProcessTime = getConcurrentProcessTime(generateTransactions(TRANSACTION_SIZE));
        var sequentialProcessTime = getSequentialProcessTime(generateTransactions(TRANSACTION_SIZE));

        assertTrue(concurrentProcessTime < sequentialProcessTime);
    }

    @Test
    void showSequentialProcessDegradationByMemoryLeak() {
        var firstTime = getSequentialProcessTime(generateTransactions(TRANSACTION_SIZE));
        var secondTime = getSequentialProcessTime(generateTransactions(TRANSACTION_SIZE));
        var thirdTime = getSequentialProcessTime(generateTransactions(TRANSACTION_SIZE));

        assertTrue(firstTime < secondTime && secondTime < thirdTime);
    }

    @Test
    void shouldGetSameResults() {
        var firstTransaction = generateTransaction();
        var secondTransaction = generateTransaction();
        var thirdTransaction = generateTransaction();

        List<Transaction> firstTransactions = new ArrayList<>();
        List<Transaction> secondTransactions = new ArrayList<>();

        firstTransactions.add(firstTransaction);
        firstTransactions.add(secondTransaction);
        firstTransactions.add(thirdTransaction);

        secondTransactions.add(firstTransaction);
        secondTransactions.add(secondTransaction);
        secondTransactions.add(thirdTransaction);

        transactionFacade.process(firstTransactions, ProcessorType.SEQUENTIAL);
        transactionFacade.process(secondTransactions, ProcessorType.CONCURRENT);

        assertEquals(firstTransactions, secondTransactions);
    }

    private long getSequentialProcessTime(List<Transaction> transactions) {
        var sequentialProcessStart = System.currentTimeMillis();
        transactionFacade.process(transactions, ProcessorType.SEQUENTIAL);
        var sequentialProcessFinish = System.currentTimeMillis();
        var sequentialMethodTime = sequentialProcessFinish - sequentialProcessStart;
        System.out.println("sequentialMethodTime is " + sequentialMethodTime);
        return sequentialMethodTime;
    }

    private long getConcurrentProcessTime(List<Transaction> transactions) {
        var concurrentProcessStart = System.currentTimeMillis();
        transactionFacade.process(transactions, ProcessorType.CONCURRENT);
        var concurrentProcessFinish = System.currentTimeMillis();
        var concurrentMethodTime = concurrentProcessFinish - concurrentProcessStart;
        System.out.println("concurrentMethodTime is " + concurrentMethodTime);
        return concurrentMethodTime;
    }
}
