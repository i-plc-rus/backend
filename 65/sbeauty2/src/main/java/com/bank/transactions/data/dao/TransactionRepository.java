package com.bank.transactions.data.dao;

import com.bank.transactions.data.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Transaction repository
 */
@Repository
public interface TransactionRepository {

    void updateTransaction(Transaction transaction);

    Collection<Transaction> getTransactions();
}
