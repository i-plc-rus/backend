package com.bank.transactions;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class TransactionProcessorTest {
	@Autowired
	private TransactionProcessor processor;

	@Autowired
	private Logger logger;

	@Autowired
	private TransactionRepository transactionRepository;

	private final static long SLA_HUGE_TRANSACTION = 3000L;

	@BeforeEach
	public void initEach(){
		transactionRepository.getTransactions().clear();
	}

	@Test
	public void testProcessTransactionsWithMultipleThreadsAndBigAmountTransactions() throws InterruptedException {
		long startDtmExecution = System.currentTimeMillis();
		Runnable runnable = () -> {
			for (int i = 0; i < 100000; i++) {
				transactionRepository.updateTransaction(generateRandomTransaction(i));
			}
		};
		List<Thread> threads = new ArrayList<>();
		for (long cnt = 5; cnt > 0; cnt--) {
			Thread thread = new Thread(runnable);
			thread.start();
			threads.add(thread);
		}
		for (Thread thread : threads) {
			thread.join();
		}
		long endDtmExecution = System.currentTimeMillis();
		long duration = endDtmExecution - startDtmExecution;

		Assertions.assertEquals(500000, transactionRepository.getTransactions().size());
		Assertions.assertTrue(duration <= SLA_HUGE_TRANSACTION);
	}

	@Test
	public void testProcessTransactions_whenNullTransactions_thenNoneSave() {
		processor.processTransactions(null);
		Assertions.assertEquals(0, transactionRepository.getTransactions().size());
	}

	@Test
	public void testProcessTransactionsWithException_whenThrowException_thenShouldInvokeLogErrorMethod() {
		Transaction transaction = new Transaction("1", 5000, "2023-01-01", "PENDING");

		TransactionRepository mockRep = Mockito.mock(TransactionRepository.class);
		Logger mockLogger = Mockito.spy(Logger.class);
		TransactionProcessor transactionProcessor = new TransactionProcessor(mockRep, mockLogger);

		RuntimeException expectedException = new RuntimeException("Some exception");

		Mockito.doThrow(expectedException).when(mockRep).updateTransaction(transaction);

		transactionProcessor.processTransactions(Collections.singletonList(transaction));

		verify(mockLogger, Mockito.times(1))
				.log("Error processing transaction: " + expectedException.getMessage(), expectedException);
	}

	@Test
	public void testProcessTransactions_whenOneTransactionInPending_thenShouldSaveOneInProcessedStatus() {
		Transaction transaction = new Transaction("1", 5000, "2023-01-01", "PENDING");
		processor.processTransactions(Collections.singletonList(transaction));
		Assertions.assertEquals(1, transactionRepository.getTransactions().size());
		TransactionStatus actualStatus = transactionRepository.getTransactions()
				.stream()
				.filter(t -> "1".equals(t.getId()))
				.map(t -> TransactionStatus.valueOf(t.getStatus()))
				.findFirst()
				.orElse(null);

		Assertions.assertEquals(TransactionStatus.PROCESSED, actualStatus);
	}

	@Test
	public void testProcessTransactions_whenOneTransactionInProcessed_thenShouldNotSave() {
		Transaction transaction = new Transaction("1", 5000, "2023-01-01", "PROCESSED");
		processor.processTransactions(Collections.singletonList(transaction));
		Assertions.assertEquals(0, transactionRepository.getTransactions().size());
	}

	@Test
	public void testProcessTransactions_whenProvisioningBigTransaction_thenShouldInvokeLog() {
		Transaction transaction = new Transaction("1", 25000, "2023-01-01", "PENDING");
		Logger mockLogger = Mockito.mock(Logger.class);
		TransactionProcessor transactionProcessor = new TransactionProcessor(transactionRepository, mockLogger);

		transactionProcessor.processTransactions(Collections.singletonList(transaction));

		verify(mockLogger, Mockito.times(1)).log(anyString());
	}

	private static Transaction generateRandomTransaction(int id) {
		return new Transaction(String.valueOf(id),
				id * 2,
				generateRandomStringDate(),
				id%2 == 0 ? TransactionStatus.PENDING.name() : TransactionStatus.PROCESSED.name());
	}

	private static String generateRandomStringDate() {
		ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
		long year = threadLocalRandom.nextLong(2020L, 2024L);
		long month = threadLocalRandom.nextLong(1L, 12L);
		long day = threadLocalRandom.nextLong(1L, 28L);
		return Joiner.on("-")
				.join(String.valueOf(year), String.valueOf(month), String.valueOf(day));
	}
}
