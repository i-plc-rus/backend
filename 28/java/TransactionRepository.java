package com.bank.transactions;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public void updateTransaction(Transaction transaction) {
        Optional<Transaction> existingTransaction = transactions.stream()
                .filter(t -> t.getId().equals(transaction.getId()))
                .findFirst();

        if (existingTransaction.isPresent()) {
            transactions.remove(existingTransaction.get());
        }

        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Возвращаем копию списка для безопасности
    }

    public Optional<Transaction> findTransactionById(String id) {
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }
}