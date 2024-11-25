package com.example.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime date;
    private String status;

    // Конструкторы, геттеры и сеттеры
    public Transaction(Long id, BigDecimal amount, LocalDateTime date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters
    // ...
}
