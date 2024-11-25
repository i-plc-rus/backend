package com.bank.transactions.service;

import com.bank.transactions.mode.Transaction;
import com.bank.transactions.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.bank.transactions.constants.TransactionConstants.*;

@Service
public class TransactionProcessingServiceImpl implements TransactionProcessingService {
	
	private static final Logger log = LoggerFactory.getLogger(TransactionProcessingServiceImpl.class);
	private static final double AMOUNT_LARGE_TRANSACTION = 10000;
	private static final int SHUTDOWN_TIMEOUT_SECONDS = 60;
	
	private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private final TransactionRepository repository;
	
	@Autowired
	public TransactionProcessingServiceImpl(TransactionRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void processTransactions(List<Transaction> transactions) {
		submitTransactionsForProcessing(transactions);
		awaitTermination();
	}
	
	private void submitTransactionsForProcessing(List<Transaction> transactions) {
		transactions.forEach(transaction -> {
			if (STATUS_PENDING.equals(transaction.getStatus())) {
				executorService.submit(() -> processTransaction(transaction));
			}
		});
	}
	
	private void processTransaction(Transaction transaction) {
		if (transaction.getAmount() > AMOUNT_LARGE_TRANSACTION) {
			log.info("Processing large transaction with ID: {}", transaction.getId());
		}
		transaction.setStatus(STATUS_PROCESSED);
		try {
			repository.updateTransaction(transaction);
		} catch (Exception e) {
			log.error("Failed to update transaction with ID: {}. Error: {}", transaction.getId(), e.getMessage(), e);
		}
	}
	
	// завершает работу executorService
	private void awaitTermination() {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
				log.warn("Executor service did not terminate in the specified time.");
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			log.error("Executor service termination interrupted.", e);
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
