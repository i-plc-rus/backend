package com.bank.transactions.transactionprocessor.service;

import com.bank.transactions.transactionprocessor.dto.TransactionDto;
import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import com.bank.transactions.transactionprocessor.repository.TransactionRepository;
import com.bank.transactions.transactionprocessor.repository.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void save(TransactionDto transaction, TransactionStatus status, TransactionType type) {
        transactionRepository.save(
                Transaction.builder()
                        .id(transaction.id())
                        .amount(transaction.amount())
                        .business_date(transaction.business_date())
                        .status(status)
                        .type(type)
                        .updated_at(LocalDateTime.now())
                        .build()
        );
    }

    public boolean isTransactionProcessed(UUID id) {
        return transactionRepository.findById(id)
                .map(transaction -> TransactionStatus.PROCESSED.equals(transaction.getStatus()))
                .orElse(false);
    }

    public void saveFailedTransaction(TransactionDto transaction) {
        this.save(transaction, TransactionStatus.FAILED, TransactionType.UNDEFINED);
    }

    public void completeTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }
}
