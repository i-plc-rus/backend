package com.example.sbertech.dao;

import com.example.sbertech.pojo.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Junior repository. Remained for profiling goal. To delete after.
 */
@Repository("TransactionRepositoryJunior")
public class TransactionRepositoryJunior implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public int countTransactions() {
        return transactions.size();
    }
}
