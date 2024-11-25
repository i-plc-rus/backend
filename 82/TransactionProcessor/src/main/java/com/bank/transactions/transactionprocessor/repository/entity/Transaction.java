package com.bank.transactions.transactionprocessor.repository.entity;

import com.bank.transactions.transactionprocessor.enums.TransactionStatus;
import com.bank.transactions.transactionprocessor.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private UUID id;

    private BigDecimal amount;

    private LocalDateTime business_date;

    private TransactionStatus status;

    private TransactionType type;

    @EqualsAndHashCode.Exclude
    private LocalDateTime updated_at;
}
