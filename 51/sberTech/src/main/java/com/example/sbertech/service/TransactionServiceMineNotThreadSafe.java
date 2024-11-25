package com.example.sbertech.service;

import com.example.sbertech.dao.TransactionRepository;
import com.example.sbertech.dao.TransactionRepositoryMineNotThreadSafe;
import com.example.sbertech.pojo.Status;
import com.example.sbertech.pojo.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Not-tread safe service. To delete after performance tests.
 */
@Service("TransactionServiceMineNotThreadSafe")
@Slf4j
public class TransactionServiceMineNotThreadSafe extends TransactionServiceMine {
    public TransactionServiceMineNotThreadSafe(
            @Qualifier("TransactionRepositoryMineNotThreadSafe") TransactionRepository repository) {
        super(repository);
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        transactions.forEach(t -> processTransaction(t));
    }

    @Override
    public void processTransaction(Transaction transaction) {
        try {
            if (transaction.isPending()) {
                repository.updateTransaction(transaction);
                transaction.setStatus(Status.PROCESSED);
            }
        } catch (Exception e) {
            log.error("Error processing transaction mine: " + e.getMessage());
        }
    }
}
