package com.bank.transactions.transactionprocessor.service.processor.amount;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "transaction.type.amount")
public record AmountTypeResolverProperty(
        BigDecimal largeThreshold,
        BigDecimal mediumThreshold,
        BigDecimal smallThreshold
) {
}
