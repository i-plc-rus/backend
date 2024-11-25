package com.bank.transactions.transactionprocessor;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.repository.TransactionRepository;
import com.bank.transactions.transactionprocessor.service.TransactionProcessingService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionProcessorApplicationTests {

	@Autowired
	private TransactionProcessingService transactionProcessingService;

	@Autowired
	private TransactionRepository transactionRepository;

	private TransactionDto createPendingTransaction() {
		return TransactionDto.builder()
				.id(UUID.randomUUID())
				.amount(BigDecimal.valueOf(Math.random() * 20_000))
				.business_date(LocalDateTime.now())
				.status(TransactionStatus.PENDING)
				.build();
	}

	@AfterEach
	public void clearRepository() {
		transactionRepository.clear();
	}

	@Test
	@DisplayName("Multithreaded processing of multiple transactions")
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	void shouldProcessTransactionsConcurrently() {
		List<TransactionDto> transactions = new ArrayList<>();
		for (int i = 0; i < 500; i++) {
			transactions.add(createPendingTransaction());
		}

		CompletableFuture<Void> future1 = CompletableFuture.runAsync(
				() -> transactionProcessingService.processTransactions(transactions.subList(0, 100))
		);
		CompletableFuture<Void> future2 = CompletableFuture.runAsync(
				() -> transactionProcessingService.processTransactions(transactions.subList(100, 200))
		);
		CompletableFuture<Void> future3 = CompletableFuture.runAsync(
				() -> transactionProcessingService.processTransactions(transactions.subList(200, 300))
		);
		CompletableFuture<Void> future4 = CompletableFuture.runAsync(
				() -> transactionProcessingService.processTransactions(transactions.subList(300, 500))
		);

		CompletableFuture.allOf(future1, future2, future3, future4).join();

		await()
				.atMost(3, TimeUnit.SECONDS)
				.until(() -> transactionRepository.count() == 500);

		transactions.forEach(transaction -> {
			assertTrue(transactionRepository.findById(transaction.id()).isPresent(),
					"All transactions should be processed in a multithreaded environment.");
		});
	}

	@Test
	@DisplayName("Performance Test: Process large number of transactions")
	@Timeout(value = 5, unit = TimeUnit.SECONDS)
	void performanceTestForTransactionProcessing() {
		List<TransactionDto> transactions = new ArrayList<>();
		for (int i = 0; i < 100_000; i++) {
			transactions.add(createPendingTransaction());
		}

		long startTime = System.currentTimeMillis();
		transactionProcessingService.processTransactions(transactions);
		long duration = System.currentTimeMillis() - startTime;

		System.out.printf("Processing 100_000 transactions took %d ms%n", TimeUnit.MILLISECONDS.toMillis(duration));
		assertTrue(TimeUnit.NANOSECONDS.toMillis(duration) < 3000,
				"Processing time should be within acceptable limits.");
	}

	@DisplayName("Parameterized Test: Process various transaction statuses")
	@ParameterizedTest(name = "{index} - Status {0}")
	@MethodSource("provideArgumentsForTransactionProcess")
	void shouldProcessTransactionBasedOnStatus(TransactionStatus status, BigDecimal amount, boolean shouldPersistInDatabase) {
		TransactionDto transaction = TransactionDto.builder()
				.id(UUID.randomUUID())
				.amount(amount)
				.business_date(LocalDateTime.now())
				.status(status)
				.build();

		List<TransactionDto> transactions = List.of(transaction);
		transactionProcessingService.processTransactions(transactions);

		await()
				.atMost(1, TimeUnit.SECONDS)
				.until(() -> transactionRepository.findById(transaction.id()).isPresent() == shouldPersistInDatabase);

		if (shouldPersistInDatabase) {
			assertTrue(transactionRepository.findById(transaction.id()).isPresent(),
					"Transaction should be processed and saved.");
		} else {
			assertTrue(transactionRepository.findById(transaction.id()).isEmpty(),
					"Transaction should not persist in database.");
		}
	}

	private static Stream<Arguments> provideArgumentsForTransactionProcess() {
		return Stream.of(
				Arguments.of(
						TransactionStatus.FAILED,
						BigDecimal.ONE,
						false
				),
				Arguments.of(
						TransactionStatus.COMPLETED,
						BigDecimal.ONE,
						false
				),
				Arguments.of(
						TransactionStatus.PENDING,
						BigDecimal.ONE,
						true
				),
				Arguments.of(
						TransactionStatus.PROCESSED,
						BigDecimal.ONE,
						false
				)
		);
	}

	@DisplayName("Parameterized Test: Process invalid transactions")
	@ParameterizedTest(name = "{index} - {0}")
	@MethodSource("provideArgumentsForInvalidTransactionProcess")
	void shouldNotProcessInvalidTransaction(String name, TransactionDto transaction) {
		List<TransactionDto> transactions = List.of(transaction);

		assertThrows(ConstraintViolationException.class, () -> transactionProcessingService.processTransactions(transactions));
	}

	private static Stream<Arguments> provideArgumentsForInvalidTransactionProcess() {
		return Stream.of(
				Arguments.of(
						"Nullable id",
						TransactionDto.builder()
								.amount(BigDecimal.TEN)
								.status(TransactionStatus.PENDING)
								.business_date(LocalDateTime.now())
								.build()
				),
				Arguments.of(
						"Amount less than zero",
						TransactionDto.builder()
								.id(UUID.randomUUID())
								.amount(BigDecimal.valueOf(-1))
								.status(TransactionStatus.PENDING)
								.business_date(LocalDateTime.now())
								.build()
				),
				Arguments.of(
						"Nullable status",
						TransactionDto.builder()
								.id(UUID.randomUUID())
								.amount(BigDecimal.TEN)
								.business_date(LocalDateTime.now())
								.build()
				),
				Arguments.of(
						"Nullable business_date",
						TransactionDto.builder()
								.id(UUID.randomUUID())
								.amount(BigDecimal.TEN)
								.status(TransactionStatus.PENDING)
								.build()
				)
		);
	}
}
