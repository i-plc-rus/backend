package com.bank.transactions.repository;

import com.bank.transactions.mode.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {
	private final ConcurrentHashMap<String, Transaction> transactions = new ConcurrentHashMap<>();
	
	// Обновляем или добавляем транзакцию
	public void updateTransaction(Transaction transaction) {
		transactions.put(transaction.getId(), transaction);
	}
	
	public List<Transaction> getTransactions() {
		return transactions.values().stream().toList();
	}
	
	public Transaction getTransaction(String id) {
		return transactions.get(id);
	}
}
