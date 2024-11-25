
package com.bank.transactions.service;

import com.bank.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/*
 * Класс используется только для тестирования производительности в сравнении с классом MapBasedTransactionProcessor.
 */

@Service("ListBasedTransactionProcessor")
public class ListBasedTransactionProcessor extends MapBasedTransactionProcessor {

    public ListBasedTransactionProcessor(@Qualifier("ListBasedRepository") TransactionRepository repository) {
        super(repository);
    }
}
