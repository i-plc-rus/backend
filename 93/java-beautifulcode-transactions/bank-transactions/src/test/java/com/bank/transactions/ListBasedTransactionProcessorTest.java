package com.bank.transactions;

import com.bank.transactions.service.TransactionProcessor;
import org.springframework.beans.factory.annotation.Qualifier;

class ListBasedTransactionProcessorTest extends AbstractTransactionProcessorTest {

    ListBasedTransactionProcessorTest(@Qualifier("ListBasedTransactionProcessor") TransactionProcessor service) {
        super(service);
    }
}
