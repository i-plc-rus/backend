package com.bank.transactions.v2;

import com.bank.common.concurent.ArrayRecursiveAction;
import com.bank.transactions.Transaction;
import java.util.function.Consumer;

/**
 * Позволяет обрабатывать транзакции параллельно в несколько потоков
 */
public class TransactionRecursiveAction extends ArrayRecursiveAction<Transaction> {

    public TransactionRecursiveAction(Transaction[] items, Consumer<Transaction> action) {
        super(items, action);
    }
}
