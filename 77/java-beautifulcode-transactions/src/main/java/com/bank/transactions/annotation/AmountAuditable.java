package com.bank.transactions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннтотация используется для маркировки методов, в которых необходим контроль суммы транзакции.
 * Используется классом {@link com.bank.transactions.aspect.TransactionMonitoringAspect TransactionMonitoringAspect}
 * @value - положительное число типа double, определяет величину транзакции для контроля. Общее дефолтное значение может уставнавливаться в настройках приложения
 * application.properties (transactions.monitoring.amount)
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AmountAuditable {
    double value() default 0;
}
