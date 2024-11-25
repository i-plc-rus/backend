package com.bank.transactions.repository;

/*
 * Класс используется только для тестирования.
 */

import com.bank.transactions.annotation.AmountAuditable;
import com.bank.transactions.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("ListBasedRepository")
public class ListBasedTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst();
    }

    @Override
    @AmountAuditable
    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public Optional<List<Transaction>> getAllTransactions() {
        List<Transaction> transactionList = List.copyOf(transactions);
        return transactionList.isEmpty() ? Optional.empty() : Optional.of(transactionList);
    }

    @Override
    public int size() {
        return transactions.size();
    }
}
