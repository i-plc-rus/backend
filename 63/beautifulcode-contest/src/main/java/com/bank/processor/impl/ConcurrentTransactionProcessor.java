
package com.bank.processor.impl;

import com.bank.dao.AsyncTransactionDaoAdapter;
import com.bank.exception.BatchSaveException;
import com.bank.processor.TransactionProcessor;
import com.bank.schema.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcurrentTransactionProcessor implements TransactionProcessor {
    private final AsyncTransactionDaoAdapter transactionDaoAdapter;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;
    private final long TIMEOUT = 5000;
    private final ChronoUnit TIMEUNIT = ChronoUnit.MILLIS;

    /**
     * Метод, содержащий основную логику асинхронной обработки транзакций.
     * Список транзакций для сохранения разделяется на пакеты, размер которых определен в настройке размера пакетов для Hibernate.
     * После этого вызывается асинхронный метод для их сохранения.
     * На проверку результата отводится фиксированный таймаут.
     * Полученные ошибки собираются в коллекцию для возврата в краткой форме (с указанием имени) и логируются в полной форме с сообщением ошибки.
     *
     * @param transactions - список транзакций.
     * @return список ошибок.
     */
    public List<BatchSaveException> processTransactions(List<TransactionRecord> transactions) {
        List<Future<Optional<Exception>>> futures = new ArrayList<>();
        int curIdx = 0;

        while (curIdx < transactions.size()) {
            int nextIdx = Math.min(curIdx + BATCH_SIZE, transactions.size());
            futures.add(transactionDaoAdapter.saveTransactions(transactions.subList(curIdx, nextIdx)));
            curIdx = nextIdx;
        }

        List<BatchSaveException> exceptions = new ArrayList<>();
        Instant deadline = Instant.now().plus(TIMEOUT, TIMEUNIT);

        for (int i = 0; i < futures.size(); i++) {
            Future<Optional<Exception>> future = futures.get(i);
            long waitTime = 0;

            if (!future.isDone()) {
                waitTime = deadline.toEpochMilli() - Instant.now().toEpochMilli();
                waitTime = Math.max(waitTime, 0);
            }

            var exceptionOptional = tryGet(future, waitTime);
            if (exceptionOptional.isPresent()) {
                Exception cause = exceptionOptional.get();
                String message = String.format(
                        "Transaction batch save for positions from %d to %d (exclusive) returned exception: "
                                + cause.getClass().getSimpleName(),
                        i * BATCH_SIZE,
                        Math.min(i * BATCH_SIZE + BATCH_SIZE, transactions.size()));
                log.warn(message + ": " + cause.getMessage());

                BatchSaveException batchSaveException = new BatchSaveException(
                        message, cause);
                exceptions.add(batchSaveException);
            }
        }
        return exceptions;
    }

    private Optional<Exception> tryGet(Future<Optional<Exception>> future, long timeout) {
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            return Optional.of(e);
        }
    }
}
