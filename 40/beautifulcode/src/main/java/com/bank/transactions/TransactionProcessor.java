
package com.bank.transactions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionProcessor {
    private static final BigDecimal THRESHOLD_AMOUNT = BigDecimal.valueOf(10000);
    private final TransactionRepository repository;

    public void processTransactions(List<Transaction> transactions) {
        try (Stream<Transaction> transactionStream = transactions.parallelStream()) {
            transactionStream.forEach(this::processTransaction);
        } catch (RuntimeException stEx) {
            errorProcessing(stEx);
        }
    }

    private void processTransaction(Transaction transaction) {
        try {
            if (isThresholdAmount(transaction)) {
                log.info(transaction.getInfoLarge());
            }
            if (isPending(transaction)) {
                transaction.setStatus(TransactionStatus.PROCESSED);
                repository.updateTransaction(transaction);
            }
        } catch (IllegalArgumentException trEx) {
            errorProcessing(trEx);
        }
    }

    private boolean isThresholdAmount(Transaction transaction) {
        return transaction.getAmount().compareTo(THRESHOLD_AMOUNT) > 0;
    }

    private boolean isPending(Transaction transaction) {
        return transaction.getStatus() == TransactionStatus.PENDING;
    }

    private void errorProcessing(Exception exception) {
        log.error("Error processing transaction: {}", exception.getMessage());
    }
}
