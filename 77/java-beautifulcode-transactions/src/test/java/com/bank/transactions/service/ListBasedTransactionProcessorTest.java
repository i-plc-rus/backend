package com.bank.transactions.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ListBasedTransactionProcessorTest extends AbstractTransactionProcessorTest {

    ListBasedTransactionProcessorTest(@Qualifier("ListBasedTransactionProcessor") TransactionProcessor service) {
        super(service);
    }
}