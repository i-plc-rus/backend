package com.bank.transactions.domain;

import java.util.List;

public class Transactions {
    private final List<Transaction> transactions;

    public Transactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Process only {@link TransactionStatus#PENDING} transactions
     * @return list of processed transactions
     */
    public List<Transaction> processed() {
        return transactions.stream()
                .filter(Transaction::isPending)
                .map(Transaction::process)
                .toList();
    }

    /**
     * Returns ids of large transactions according to {@code limit}
     * @param limit all transactions with amount greater than {@code limit} considered as larges
     * @return ids of large transactions
     */
    public List<String> largeTransactionIds(int limit) {
        return transactions.stream()
                .filter(t -> t.isLarge(limit))
                .map(Transaction::getId)
                .toList();
    }
}
