package com.bank.transactions.transactionprocessor.service.processor.amount;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import com.bank.transactions.transactionprocessor.service.processor.TransactionTypeResolver;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionAmountTypeResolver implements TransactionTypeResolver {

    private final BigDecimal largeThreshold;

    private final BigDecimal mediumThreshold;

    private final BigDecimal smallThreshold;

    public TransactionAmountTypeResolver(AmountTypeResolverProperty property) {
        this.largeThreshold = property.largeThreshold();
        this.mediumThreshold = property.mediumThreshold();
        this.smallThreshold = property.smallThreshold();
    }

    @Override
    public TransactionType getType(TransactionDto transaction) {
        BigDecimal amount = transaction.amount();

        if (amount == null) {
            return TransactionType.UNDEFINED;
        }

        if (amount.compareTo(largeThreshold) > 0) {
            return TransactionType.LARGE;
        } else if (amount.compareTo(mediumThreshold) > 0) {
            return TransactionType.MEDIUM;
        } else if (amount.compareTo(smallThreshold) >= 0) {
            return TransactionType.SMALL;
        } else {
            return TransactionType.UNDEFINED;
        }
    }
}
