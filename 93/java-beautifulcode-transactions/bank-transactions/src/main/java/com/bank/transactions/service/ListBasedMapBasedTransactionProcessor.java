package com.bank.transactions.service;

/*
 * Класс используется только для тестирования производительности в сравнении с классом MapBasedTransactionProcessor.
 */

import com.bank.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ListBasedTransactionProcessor")
public class ListBasedMapBasedTransactionProcessor extends MapBasedTransactionProcessor {

    public ListBasedMapBasedTransactionProcessor(@Qualifier("ListBasedRepository") TransactionRepository repository) {
        super(repository);
    }
}
