package com.bank.transactions.service;

import com.bank.transactions.mode.Transaction;

import java.util.List;

public interface TransactionProcessingService {
	void processTransactions(List<Transaction> transactions);
}
