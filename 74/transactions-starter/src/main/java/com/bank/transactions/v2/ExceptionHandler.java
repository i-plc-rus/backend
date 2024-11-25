package com.bank.transactions.v2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Аспект для перехвата и обработки исключений
 */
@Aspect
public class ExceptionHandler {

    private final Logger logger;

    public ExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    /**
     * Срез для методов {@link com.bank.transactions.v2.TransactionProcessor}
     */
    @Pointcut("execution(* com.bank.transactions.v2.TransactionProcessor.*(..))")
    public void transactionProcessorOperation() {
    }

    /**
     * Обработчик исключений
     */
    @Around("transactionProcessorOperation()")
    public Object handleException(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.log(this, "Something went wrong: " + throwable.getMessage(), throwable);
        }
        return null;
    }
}
