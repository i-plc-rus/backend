
package com.bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(schema = "account", name = "transaction")
public class Transaction {
    @Id
    private UUID id;
    private Double amount;
    private OffsetDateTime date;
    private TransactionStatus status;
}
