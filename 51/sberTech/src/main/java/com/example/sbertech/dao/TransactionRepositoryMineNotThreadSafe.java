package com.example.sbertech.dao;

import com.example.sbertech.pojo.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Repository for not thread safe handling. Exists for profiling goal. To delete after.
 */
@Component
@Qualifier("TransactionRepositoryMineNotThreadSafe")
public class TransactionRepositoryMineNotThreadSafe extends TransactionRepositoryMine {

    @Override
    public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
