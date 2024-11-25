package com.bank.transactions.service;

import com.bank.transactions.model.Transaction;
import com.bank.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service("MapBasedTransactionProcessor")
public class MapBasedTransactionProcessor implements TransactionProcessor {
    private final TransactionRepository transactionRepository;

    public MapBasedTransactionProcessor(@Qualifier("MapBasedRepository") TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        if (id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        return transactionRepository
                .getTransactionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction with id \" + id + \" was not found"));
    }

    @Override
    public List<Transaction> getAllTransactions(int count) {
        List<Transaction> allTransactions = new ArrayList<>(transactionRepository.getAllTransactions().orElseGet(ArrayList::new));
        return allTransactions.stream().limit(count).collect(Collectors.toList());
    }

    @Override
    @Async("FixedThreadPoolTaskExecutor")
    public CompletableFuture<Transaction> addTransaction(double amount) {
        if (amount == 0) {
            throw new IllegalArgumentException("Amount cannot be zero");
        }
        Transaction transaction = new Transaction(BigDecimal.valueOf(amount));
        transactionRepository.saveTransaction(transaction);
        return CompletableFuture.completedFuture(transaction);
    }

    @Override
    public void processTransactions(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Transactions ids cannot be null or empty");
        }

        ids.forEach(this::processTransaction);
    }

    @Override
    @Async("FixedThreadPoolTaskExecutor")
    public void processTransaction(Long id) {
        Transaction transaction = getTransactionById(id);

        if (transaction.getStatus() != Transaction.Status.PENDING) {
            throw new IllegalArgumentException("Transaction with id " + transaction.getId() + " must have state PENDING");
        }

        transaction.setStatus(Transaction.Status.PROCESSED);
        // При in-memory хранении данных метод save() может не вызываться
        transactionRepository.saveTransaction(transaction);
    }

    @Override
    public int getTransactionsCount() {
        return transactionRepository.size();
    }
}
