package com.bank.dao.impl;

import com.bank.dao.AsyncTransactionDaoAdapter;
import com.bank.dao.TransactionDao;
import com.bank.mapper.TransactionMapper;
import com.bank.schema.TransactionRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Адаптер, обрабатывающий ошибки при сохранении транзакций.
 * Маппинг выведен на этот уровень из соображений экономии времени за счет многопоточной обработки (пусть и незначительной).
 */
@Component
@RequiredArgsConstructor
public class AsyncTransactionDaoAdapterImpl implements AsyncTransactionDaoAdapter {
    private final TransactionMapper mapper;
    private final TransactionDao transactionDao;

    @Override
    public Future<Optional<Exception>> saveTransactions(List<TransactionRecord> records) {
        try {
            transactionDao.saveTransactions(records.stream()
                    .map(mapper::toModel)
                    .toList()
            );

        } catch (Exception e) {
            return CompletableFuture.completedFuture(Optional.of(e));

        }
        return CompletableFuture.completedFuture(Optional.empty());
    }
}
