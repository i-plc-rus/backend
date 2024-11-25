package com.bank.transactions;

import com.bank.transactions.mode.Transaction;
import com.bank.transactions.repository.TransactionRepository;
import com.bank.transactions.service.TransactionProcessingService;
import com.bank.transactions.service.TransactionProcessingServiceImpl;
import com.bank.transactions.source_code.Logger;
import com.bank.transactions.source_code.TransactionProcessor;
import com.bank.transactions.source_code.TransactionRepositoryOld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.bank.transactions.constants.TransactionConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionProcessingTest {
	
	private TransactionRepository repository;
	private TransactionProcessingService service;
	private TransactionProcessor serviceOld;
	
	@BeforeEach
	void setUp() {
		repository = new TransactionRepository();
		service = new TransactionProcessingServiceImpl(repository);
		serviceOld = new TransactionProcessor(new TransactionRepositoryOld(), new Logger());
	}
	
	@Test
	void testProcessesPendingTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction("1", 1000.0, "2024-10-27", STATUS_PENDING));
		transactions.add(new Transaction("2", 500.0, "2024-10-27", STATUS_PROCESSED));
		transactions.add(new Transaction("3", 2000.0, "2024-10-27", STATUS_PENDING));
		transactions.add(new Transaction("4", 500.0, "2024-10-27", STATUS_COMPLETED));
		
		service.processTransactions(transactions);
		
		assertEquals(STATUS_PROCESSED, repository.getTransactions().get(0).getStatus());
		assertEquals(STATUS_PROCESSED, repository.getTransactions().get(1).getStatus());
		
		assertEquals(2, repository.getTransactions().size());
	}
	
	@Test
	void testHandlesLargeTransaction() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction("1", 50000.0, "2024-10-27", STATUS_PENDING));
		
		service.processTransactions(transactions);
		
		assertEquals(STATUS_PROCESSED, repository.getTransaction("1").getStatus());
	}
	
	@Test
	void testReturnsAllTransactions() {
		Transaction transaction1 = new Transaction("1", 1000.0, "2024-10-27", STATUS_PENDING);
		Transaction transaction2 = new Transaction("2", 500.0, "2024-10-27", STATUS_PROCESSED);
		repository.updateTransaction(transaction1);
		repository.updateTransaction(transaction2);
		
		List<Transaction> result = repository.getTransactions();
		
		assertEquals(2, result.size());
		assertTrue(result.contains(transaction1));
		assertTrue(result.contains(transaction2));
	}
	
	/** Сравнение времени выполнения операций при 1_000_000 данных с исходным кодом */
	@Test
	void testPerformanceComparison() {
		int numberOfTransactions = 1_000_000;
		List<Transaction> transactions = createTestTransactions(numberOfTransactions);
		
		// Время выполнения исходного кода
		long startOld = System.nanoTime();
		serviceOld.processTransactions(transactions);
		long durationOld = System.nanoTime() - startOld;
		
		// Время выполнения исправленного кода
		long startNew = System.nanoTime();
		service.processTransactions(transactions);
		long durationNew = System.nanoTime() - startNew;
		
		System.out.printf("Time taken by old method: %s ns\n", durationOld);
		System.out.printf("Time taken by new method: %s ns\n", durationNew);
		assertTrue(durationNew < durationOld, "New method should be faster than the old method");
	}
	
	private List<Transaction> createTestTransactions(int count) {
		List<Transaction> transactions = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			transactions.add(new Transaction(String.valueOf(i), 5000.0, "2024-10-27", STATUS_PENDING));
		}
		return transactions;
	}
}
