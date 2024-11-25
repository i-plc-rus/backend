package com.bank.transactions.refactored;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collection;

/// Используем [Slf4j] и [RequiredArgsConstructor] из Lombok, для логирвоания и сокращения кода, соответственно.
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProcessor {
    /// Определяет предел большой транзакции
    private final long LARGE_TRANSACTION_THRESHOLD = 10000;
    private final TransactionRepository repository;

    /// Принимает коллекцию транзакций, проверяет её на `null`, затем передаёт их параллельно для дальнейшей обработки
    /// в методе `processTransactions`
    public void processTransactions(Collection<Transaction> transactions) {
        if (nullCheck(transactions, "Collection of transactions")) return;
        transactions.stream().parallel().forEach(this::processTransaction);
    }

    /// Возвращает коллекцию обработанных транзакций
    public Collection<Transaction> getTransactions() {
        return repository.getTransactions();
    }

    public void clearTransactions() {
        repository.clearTransactions();
    }

    /// Принимает транзакцию, проверяет её на `null`, если транзакция ожидает обработки, то проверяет `amount` и `date`,
    /// далее помечает транзакцию как обработанную и отправляет на добавление в репозиторий. После добавления
    /// проверяет транзакцию на превышение лимита, если лимит превышен, то выводится сообщение в лог
    private void processTransaction(Transaction transaction) {
        if (nullCheck(transaction, "Transaction")) return;
        if (checkTransactionStatus(transaction)) return;
        if (checkTransactionAmount(transaction)) return;
        if (checkTransactionDate(transaction)) return;
        transaction.setStatus(TransactionStatus.PROCESSED);
        repository.updateTransaction(transaction);
        checkTransactionAmountThreshold(transaction);
    }

    /// Проверяет объект на `null` и выводит соответствующее сообщение в лог
    private boolean nullCheck(Object object, String typeOfObject) {
        if (object == null) {
            log.warn("{} is null! Nothing to process! Check supplier!", typeOfObject);
            return true;
        } else return false;
    }

    /// Проверяет сумму транзакции на отрицательность и равенство нулю и. При
    /// несоблюдении условий отклоняет транзакцию
    private boolean checkTransactionAmount(Transaction transaction) {
        long amount = transaction.getAmount();
        if (amount < 0) {
            log.warn("Amount is negative! {} has been cancelled! Check supplier!", transaction);
            transaction.setStatus(TransactionStatus.CANCELLED);
            return true;
        } else if (amount == 0) {
            log.warn("Amount is zero! {} has been cancelled! Check supplier!", transaction);
            transaction.setStatus(TransactionStatus.CANCELLED);
            return true;
        }
        return false;
    }

    /// Проверяет превышение лимита большой транзакции и выводит сообщение в лог. (Операции разделены с
    /// `checkTransactionAmount`, так как нет смысла писать сообщение в лог о транзакции до присвоения `id`)
    private void checkTransactionAmountThreshold(Transaction transaction) {
        if (transaction.getAmount() > LARGE_TRANSACTION_THRESHOLD) {
            log.info("Processed large {}.", transaction);
        }
    }

    /// Проверяет дату транзакции на `null` и относится ли она к прошлому или нет (транзакции из будущего отклоняет)
    private boolean checkTransactionDate(Transaction transaction) {
        ZonedDateTime date = transaction.getDate();
        if (date == null) {
            log.warn("Date is null! {} has been cancelled! Check supplier!", transaction);
            transaction.setStatus(TransactionStatus.CANCELLED);
            return true;
        } else if (date.isAfter(ZonedDateTime.now())) {
            log.warn("Date is in the future! {} has been cancelled! Check supplier and system time on server!", transaction);
            transaction.setStatus(TransactionStatus.CANCELLED);
            return true;
        }
        return false;
    }

    /// Проверяет статус транзакции на `null` и является ли статус ожидающим обработки
    private boolean checkTransactionStatus(Transaction transaction) {
        TransactionStatus status = transaction.getStatus();
        if (status == null) {
            log.warn("Status is null! {} has been cancelled! Check supplier!", transaction);
            transaction.setStatus(TransactionStatus.CANCELLED);
            return true;
        } else return !TransactionStatus.PENDING.equals(status);
    }
}
