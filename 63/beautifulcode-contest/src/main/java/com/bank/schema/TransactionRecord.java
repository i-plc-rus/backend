package com.bank.schema;

import com.bank.model.TransactionStatus;
import com.bank.validation.NotNullOrZeroConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionRecord(
    @NotNull UUID id,
    @NotNullOrZeroConstraint Double amount,
    @PastOrPresent OffsetDateTime date,
    TransactionStatus status
) {}
