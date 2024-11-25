package com.bank.transactions.transactionprocessor.dto;

import com.bank.transactions.transactionprocessor.enums.TransactionStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransactionDto(
        @NotNull UUID id,
        @Min(0) BigDecimal amount,
        @NotNull LocalDateTime business_date,
        @NotNull TransactionStatus status
) {
}
