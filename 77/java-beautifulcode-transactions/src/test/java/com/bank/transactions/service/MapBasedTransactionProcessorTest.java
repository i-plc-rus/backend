package com.bank.transactions.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MapBasedTransactionProcessorTest extends AbstractTransactionProcessorTest {

    MapBasedTransactionProcessorTest(@Qualifier("MapBasedTransactionProcessor") TransactionProcessor service) {
        super(service);
    }
}