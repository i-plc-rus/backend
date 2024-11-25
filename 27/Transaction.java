package com.bank.transactions;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public record Transaction(
        @Id @NotNull String id,
        @NotNull double amount,
        @NotEmpty String date,
        @NotEmpty String status) {
}
