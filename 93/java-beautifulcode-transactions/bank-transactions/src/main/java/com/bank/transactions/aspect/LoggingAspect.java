package com.bank.transactions.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Класс предназначен для централизованного логирования.
 * Целевые методы для логирования должны иметь аннотацию
 * {@link com.bank.transactions.annotation.Loggable @Loggable}
 */

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(com.bank.transactions.annotation.Loggable)")
    static void loggableMethods() {
    }

    @Before("loggableMethods()")
    public void debugLogging(JoinPoint point) {
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        log.debug("Executing method: {}() with args: {}", methodName, Arrays.toString(args));
    }
}
