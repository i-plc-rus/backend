package com.bank.transactions.aspect;

import com.bank.transactions.annotation.AmountAuditable;
import com.bank.transactions.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Класс предназначен для централизованного контроля суммы транзакции.
 * Целевые методы должны иметь аннотацию
 * {@link com.bank.transactions.annotation.AmountAuditable @AmountAuditable}
 */

@Slf4j
@Aspect
@Component
public class TransactionMonitoringAspect {
    private final double defaultAuditableAmount;

    public TransactionMonitoringAspect(@Value("${transactions.monitoring.amount}") double amount) {
        this.defaultAuditableAmount = amount;
    }

    @Pointcut("@annotation(com.bank.transactions.annotation.AmountAuditable) && args(transaction))")
    public void amountAuditableMethods(Transaction transaction) {
    }

    @Before(value = "amountAuditableMethods(transaction)", argNames = "point,transaction")
    public void auditTransactionAmount(JoinPoint point, Transaction transaction) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        AmountAuditable annotation = method.getAnnotation(AmountAuditable.class);
        double annotationAuditableAmount = annotation.value();

        if (annotationAuditableAmount <= 0 && defaultAuditableAmount > 0) {
            annotationAuditableAmount = defaultAuditableAmount;
        }

        double transactionAmount = transaction.getAmount().doubleValue();
        if (Math.abs(transactionAmount) >= annotationAuditableAmount) {
            log.warn("Processing large transaction amount: id={}, amount={}", transaction.getId(), transactionAmount);
        }
    }
}
