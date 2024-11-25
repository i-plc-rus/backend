
package com.bank.service;

import com.bank.exception.BatchSaveException;
import com.bank.processor.TransactionProcessor;
import com.bank.schema.TransactionRecord;
import com.bank.util.DataProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor processor;
    private static final Logger log = Logger.getLogger(TransactionProcessorTest.class.getName());

    @Nested
    class ProcessingTimeTest {
        @ParameterizedTest
        @ValueSource(ints = {1,20,50,100,1000,10_000})
        public void testProcessTransactions(int amount) {
            List<TransactionRecord> records = new ArrayList<>(amount);
            IntStream.range(0, amount).forEach(count -> records.add(DataProvider.getRecord()));

            Instant start = Instant.now();
            List<BatchSaveException> exceptions = processor.processTransactions(records);
            Instant end = Instant.now();

            assertTrue(exceptions.isEmpty());
            logProcessingTime(amount, end.toEpochMilli() - start.toEpochMilli());
        }


    }

    private void logProcessingTime(int amount, long millis) {
        log.info(String.format("Time to process %d transactions: %d ms", amount, millis));
    }
}
